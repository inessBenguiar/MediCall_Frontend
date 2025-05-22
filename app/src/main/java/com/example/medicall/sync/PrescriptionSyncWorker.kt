package com.example.medicall.sync

import com.example.medicall.repository.PrescriptionRepository
import com.example.medicall.repository.RemotePrescriptionRepository



import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.medicall.util.ConnectivityManager
import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent.inject

class PrescriptionSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams) {

    private val localRepository: PrescriptionRepository by inject(PrescriptionRepository::class.java)
    private val remoteRepository: RemotePrescriptionRepository by inject(
        RemotePrescriptionRepository::class.java
    )
    private val connectivityManager: ConnectivityManager by inject(ConnectivityManager::class.java)

    override fun doWork(): Result {
        val syncManager = PrescriptionSyncManager(
            applicationContext,
            localRepository,
            remoteRepository,
            connectivityManager
        )

        return try {
            // Run in blocking mode since Worker doesn't support coroutines directly
            runBlocking {
                syncManager.syncPrescriptions()
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}