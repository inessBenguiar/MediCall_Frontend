package com.example.medicall.viewmodel

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicall.entity.CreatePrescriptionDto
import com.example.medicall.entity.Medication
import com.example.medicall.entity.MedicationDto
import com.example.medicall.entity.Prescription
import com.example.medicall.entity.local.PrescriptionWithMedications
import com.example.medicall.repository.PrescriptionRepository
import com.example.medicall.service.PrescriptionApiService
import com.example.medicall.sync.PrescriptionSyncManager
import com.example.medicall.util.ConnectivityManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class PrescriptionViewModel(
    private val localRepository: PrescriptionRepository,
    val doctorId: Long,
    private val apiService: PrescriptionApiService?,
    private val syncManager: PrescriptionSyncManager?,
    private val connectivityManager: ConnectivityManager?
) : ViewModel() {
    private val TAG = "PrescriptionViewModel"
    private val _prescriptions = MutableStateFlow<List<PrescriptionWithMedications>>(emptyList())
    val prescriptions: StateFlow<List<PrescriptionWithMedications>> = _prescriptions.asStateFlow()
    private val _currentPrescription = MutableStateFlow<PrescriptionWithMedications?>(null)
    val currentPrescription: StateFlow<PrescriptionWithMedications?> = _currentPrescription.asStateFlow()
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    private val _syncStatus = MutableStateFlow<String?>(null)
    val syncStatus: StateFlow<String?> = _syncStatus.asStateFlow()
    private val _isPdfLoading = MutableStateFlow(false)
    val isPdfLoading: StateFlow<Boolean> = _isPdfLoading.asStateFlow()
    private val _pdfDownloadStatus = MutableStateFlow<String?>(null)
    val pdfDownloadStatus: StateFlow<String?> = _pdfDownloadStatus.asStateFlow()

    private val defaultAppointmentId: Long = 2L
    init {
        loadPrescriptions()
    }

    private fun loadPrescriptions() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                localRepository.getByDoctor(doctorId.toString()).collect { prescriptions ->
                    _prescriptions.value = prescriptions
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _error.value = "Failed to load prescriptions: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun getPrescription(id: Long) {
        viewModelScope.launch {
            try {
                _error.value = null
                _isLoading.value = true
                val prescription = localRepository.getById(id)
                _isLoading.value = false

                if (prescription != null) {
                    _currentPrescription.value = prescription
                } else {
                    _error.value = "Prescription not found"
                }
            } catch (e: Exception) {
                _error.value = "Failed to load prescription: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun clearCurrentPrescription() {
        _currentPrescription.value = null
    }

    suspend fun getById(id: Long): PrescriptionWithMedications? {
        return localRepository.getById(id)
    }

    fun savePrescription(
        id: Long? = null,
        patientName: String,
        diagnosis: String,
        instructions: String,
        date: String,
        medications: List<Medication>,
        patientAge: String? = null,
        appointmentId: Long? = defaultAppointmentId
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                if (medications.isEmpty()) {
                    _error.value = "At least one medication is required"
                    _isLoading.value = false
                    return@launch
                }
                val prescription = Prescription(
                    id = id ?: 0L,
                    patientName = patientName,
                    patientAge = patientAge?.toIntOrNull(),
                    doctorId = doctorId,
                    diagnosis = diagnosis,
                    instructions = instructions,
                    date = date,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    isSynced = false,
                    remoteId = null
                )
                val prescriptionId = if (id == null) {
                    localRepository.insert(prescription, medications)
                } else {
                    localRepository.update(prescription, medications)
                    id
                }
                if (id != null) {
                    localRepository.markAsUnsynced(id)

                     if (connectivityManager?.isNetworkAvailable() == true && apiService != null) {
                        try {
                            syncEditedPrescription(id, patientName, diagnosis, instructions, medications)
                        } catch (e: Exception) {
                            Log.e(TAG, "Failed to sync updated prescription: ${e.message}", e)
                             }
                    }
                }
                _error.value = null
                _isLoading.value = false
                _currentPrescription.value = null
                loadPrescriptions()
            } catch (e: Exception) {
                _error.value = "Failed to save prescription: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    private suspend fun syncEditedPrescription(
        id: Long,
        patientName: String,
        diagnosis: String,
        instructions: String,
        medications: List<Medication>
    ) {
        if (apiService == null || connectivityManager?.isNetworkAvailable() != true) {
            return
        }

        try {
            _syncStatus.value = "Synchronisation en cours..."

            val existingPrescription = localRepository.getById(id)
            if (existingPrescription == null || existingPrescription.prescription.remoteId == null) {
                Log.d(TAG, "No remote ID found for prescription $id, skipping remote update")
                return
            }

            val remoteId = existingPrescription.prescription.remoteId

            val medicationDtos = medications.map { med ->
                MedicationDto(
                    name = med.name,
                    dosage = med.dosage,
                    frequency = med.frequency,
                    duration = med.duration ?: "7 days"
                )
            }

            val prescriptionDto = CreatePrescriptionDto(
                patientId = localRepository.getPatientIdFromName(patientName),
                doctorId = doctorId,
                appointment_id = defaultAppointmentId,
                diagnosis = diagnosis,
                instructions = instructions,
                medications = medicationDtos
            )

            val result = apiService.updatePrescription(remoteId, prescriptionDto)

            localRepository.markAsSynced(id, result.id)
            _syncStatus.value = "Synchronisation terminée"

        } catch (e: Exception) {
            _syncStatus.value = "Synchronisation échouée"
            Log.e(TAG, "Error syncing edited prescription: ${e.message}", e)
            throw e
        } finally {
            kotlinx.coroutines.delay(3000)
            _syncStatus.value = null
        }
    }

    fun addPrescription(prescription: Prescription, medications: List<Medication>) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                localRepository.insert(prescription, medications)
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = "Failed to add prescription: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun updatePrescription(prescription: Prescription, medications: List<Medication>) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                localRepository.update(prescription, medications)
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = "Failed to update prescription: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun deletePrescription(prescription: Prescription) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _syncStatus.value = "Suppression en cours..."


                localRepository.delete(prescription)

                if (connectivityManager?.isNetworkAvailable() == true && apiService != null && prescription.remoteId != null) {
                    try {
                        apiService.deletePrescription(prescription.remoteId)
                        _syncStatus.value = "Suppression terminée"
                    } catch (e: HttpException) {
                        Log.e(TAG, "HTTP error when deleting remote prescription: ${e.code()}", e)
                        _syncStatus.value = "Suppression locale uniquement"
                    } catch (e: Exception) {
                        Log.e(TAG, "Error deleting remote prescription: ${e.message}", e)
                        _syncStatus.value = "Suppression locale uniquement"
                    }
                } else {
                    _syncStatus.value = "Suppression locale uniquement"
                }

                _isLoading.value = false


                kotlinx.coroutines.delay(3000)
                _syncStatus.value = null
            } catch (e: Exception) {
                _error.value = "Failed to delete prescription: ${e.message}"
                _isLoading.value = false
                _syncStatus.value = "Suppression échouée"
                kotlinx.coroutines.delay(3000)
                _syncStatus.value = null
            }
        }
    }

    fun syncData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _syncStatus.value = "Synchronisation en cours..."
                syncManager?.syncPrescriptions()
                _syncStatus.value = "Synchronisation terminée"
                _isLoading.value = false
                kotlinx.coroutines.delay(3000)
                _syncStatus.value = null
            } catch (e: Exception) {
                _error.value = "Failed to sync data: ${e.message}"
                _isLoading.value = false
                _syncStatus.value = "Synchronisation échouée"
                kotlinx.coroutines.delay(3000)
                _syncStatus.value = null
            }
        }
    }
    fun downloadPrescriptionPdf(id: Long, context: Context): Boolean {
        if (apiService == null || connectivityManager?.isNetworkAvailable() != true) {
            _error.value = "Cannot download PDF: No network connection or API service unavailable"
            return false
        }

        viewModelScope.launch {
            try {
                _isPdfLoading.value = true
                _pdfDownloadStatus.value = "Téléchargement du PDF..."
                _error.value = null

                val prescription = localRepository.getById(id)?.prescription
                val fileName = buildPdfFileName(prescription, id)

                val responseBody = apiService.downloadPrescriptionPdf(id)


                val success = savePdfToStorage(context, responseBody, fileName)

                if (success) {
                    _pdfDownloadStatus.value = "PDF téléchargé avec succès"
                } else {
                    _error.value = "Échec de l'enregistrement du PDF"
                }

                _isPdfLoading.value = false


                kotlinx.coroutines.delay(3000)
                _pdfDownloadStatus.value = null

            } catch (e: Exception) {
                Log.e(TAG, "Error downloading PDF: ${e.message}", e)
                _error.value = "Échec du téléchargement: ${e.message}"
                _isPdfLoading.value = false


                kotlinx.coroutines.delay(3000)
                _pdfDownloadStatus.value = null
            }
        }
        return true
    }

    private fun buildPdfFileName(prescription: Prescription?, id: Long): String {
        var fileName = "prescription_$id"

        if (prescription != null) {
            val patientName = prescription.patientName.replace(" ", "_")
            fileName += "_${patientName}"
        }

        return "${fileName}.pdf"
    }
    private fun savePdfToStorage(context: Context, responseBody: ResponseBody, fileName: String): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveFileUsingMediaStore(context, responseBody, fileName)
            } else {
                saveFileToExternalStorage(responseBody, fileName)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error saving PDF: ${e.message}", e)
            false
        }
    }

    private fun saveFileUsingMediaStore(context: Context, responseBody: ResponseBody, fileName: String): Boolean {
        val contentResolver: ContentResolver = context.contentResolver

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }

        val contentUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Files.getContentUri("external")
        }

        val uri: Uri? = contentResolver.insert(contentUri, contentValues)

        return if (uri != null) {
            contentResolver.openOutputStream(uri)?.use { outputStream ->
                responseBody.byteStream().use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                contentResolver.update(uri, contentValues, null, null)
            }

            Log.d(TAG, "File saved successfully to Downloads: $fileName")
            true
        } else {
            Log.e(TAG, "Failed to create new MediaStore record.")
            false
        }
    }



    private fun saveFileToExternalStorage(responseBody: ResponseBody, fileName: String): Boolean {
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        if (!downloadsDir.exists()) {
            downloadsDir.mkdirs()
        }

        val file = File(downloadsDir, fileName)
        var outputStream: OutputStream? = null

        return try {
            outputStream = FileOutputStream(file)
            responseBody.byteStream().use { inputStream ->
                inputStream.copyTo(outputStream)
            }
            Log.d(TAG, "File saved successfully to Downloads: ${file.absolutePath}")
            true
        } catch (e: IOException) {
            Log.e(TAG, "Error saving file: ${e.message}", e)
            false
        } finally {
            outputStream?.close()
        }
    }

}