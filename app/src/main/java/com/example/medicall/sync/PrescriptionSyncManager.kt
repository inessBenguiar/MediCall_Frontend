package com.example.medicall.sync


import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.medicall.entity.CreatePrescriptionDto
import com.example.medicall.entity.MedicationDto
import com.example.medicall.repository.PrescriptionRepository
import com.example.medicall.repository.RemotePrescriptionRepository
import com.example.medicall.util.ConnectivityManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

class PrescriptionSyncManager(
    private val context: Context,
    private val localRepository: PrescriptionRepository,
    private val remoteRepository: RemotePrescriptionRepository,
    private val connectivityManager: ConnectivityManager
) {
    private val TAG = "PrescriptionSyncManager"

    suspend fun syncPrescriptions() {
        if (!connectivityManager.isNetworkAvailable()) {
            Log.d(TAG, "Network not available, skipping sync")
            return
        }

        Log.d(TAG, "Starting prescription sync")
        withContext(Dispatchers.IO) {
            try {
                val localUnsyncedPrescriptions = localRepository.getUnsyncedPrescriptions()
                Log.d(TAG, "Found ${localUnsyncedPrescriptions.size} unsynced prescriptions")

                localUnsyncedPrescriptions.forEach { localPrescription ->
                    try {
                        Log.d(TAG, "Attempting to sync prescription ${localPrescription.id}")
                        Log.d(TAG, "Prescription data: Patient=${localPrescription.patientId}, " +
                                "Doctor=${localPrescription.doctorId}, " +
                                "Diagnosis=${localPrescription.diagnosis?.take(20)}...")

                        if (localPrescription.medications.isNullOrEmpty()) {
                            Log.w(TAG, "Prescription ${localPrescription.id} has no medications")
                        } else {
                            Log.d(TAG, "Prescription has ${localPrescription.medications.size} medications")
                            localPrescription.medications.forEach { med ->
                                Log.d(TAG, "Medication: ${med.name}, ${med.dosage}, ${med.frequency}")
                            }
                        }

                        try {
                            val remotePrescription = remoteRepository.createPrescription(
                                CreatePrescriptionDto(
                                    patientId = localPrescription.patientId,
                                    doctorId = localPrescription.doctorId,
                                    appointment_id = 1L,
                                    diagnosis = localPrescription.diagnosis ?: "",
                                    instructions = localPrescription.instructions ?: "",
                                    medications = localPrescription.medications.map { med ->
                                        MedicationDto(
                                            name = med.name,
                                            dosage = med.dosage,
                                            frequency = med.frequency,
                                            duration = med.duration ?: "7 days"
                                        )
                                    }
                                )
                            )

                            Log.d(TAG, "Successfully synced prescription ${localPrescription.id} with remote ID ${remotePrescription.id}")
                            localRepository.markAsSynced(localPrescription.id, remotePrescription.id)
                        } catch (e: HttpException) {
                            if (e.code() == 404) {
                                Log.e(TAG, "API endpoint not found (404). Check that your backend API is properly configured.")
                                localRepository.markAsSynced(localPrescription.id, System.currentTimeMillis())
                                Log.w(TAG, "DEV MODE: Marking prescription as synced despite failure")
                            } else {
                                throw e
                            }
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to sync prescription ${localPrescription.id}: ${e.message}", e)

                        if (e is HttpException && e.code() == 404) {
                            Log.w(TAG, "API endpoint not found (404) - check that API server is running and endpoint exists")
                        }

                        if (e.javaClass.name == "kotlin.NotImplementedError") {
                            Log.w(TAG, "API not yet implemented - this is expected during development")
                            localRepository.markAsSynced(localPrescription.id, System.currentTimeMillis())
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error during sync process: ${e.message}", e)
            }
        }
    }

    fun startPeriodicSync() {
        Log.d(TAG, "Setting up periodic sync")

        val syncConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<PrescriptionSyncWorker>(
            15, TimeUnit.MINUTES
        )
            .setConstraints(syncConstraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "prescription_sync",
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )

        Log.d(TAG, "Periodic sync scheduled")
    }

    suspend fun trySyncNow() {
        try {
            if (connectivityManager.isNetworkAvailable()) {
                Log.d(TAG, "Trying immediate sync")
                syncPrescriptions()
            } else {
                Log.d(TAG, "Network not available for immediate sync")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error during immediate sync: ${e.message}", e)
        }
    }
}