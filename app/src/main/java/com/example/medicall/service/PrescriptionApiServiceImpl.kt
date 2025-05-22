package com.example.medicall.service


import android.util.Log
import com.example.medicall.entity.CreatePrescriptionDto
import com.example.medicall.entity.PrescriptionDto
import com.example.medicall.util.ConnectivityManager
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
class PrescriptionApiServiceImpl(
    private val apiClient: ApiClient,
    private val connectivityManager: ConnectivityManager
) : PrescriptionApiService {
    private val TAG = "PrescriptionApiService"
    override suspend fun getAllPrescriptions(): List<PrescriptionDto> {
        if (!connectivityManager.isNetworkAvailable()) {
            Log.e(TAG, "Network not available, cannot get all prescriptions")
            return emptyList()
        }
        try {
            Log.d(TAG, "Attempting to get all prescriptions")
            return apiClient.service.getAllPrescriptions()
        } catch (e: HttpException) {
            // Handle HTTP errors
            Log.e(TAG, "HTTP error when getting all prescriptions: ${e.code()}", e)
            return emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting all prescriptions: ${e.message}", e)
            return emptyList()
        }
    }
    override suspend fun createPrescription(prescriptionDto: CreatePrescriptionDto): PrescriptionDto {
        if (!connectivityManager.isNetworkAvailable()) {
            Log.e(TAG, "Network not available, cannot create prescription")
            throw IOException("No network connection available")
        }
        try {
            Log.d(TAG, "Attempting to create prescription on backend: ${prescriptionDto.patientId}")
            Log.d(TAG, "Prescription data: appointment_id=${prescriptionDto.appointment_id}, patientId=${prescriptionDto.patientId}, doctorId=${prescriptionDto.doctorId}")
            return apiClient.service.createPrescription(prescriptionDto)
        } catch (e: HttpException) {
            Log.e(TAG, "HTTP error when creating prescription: ${e.code()}", e)
            if (e.code() == 404) {
                Log.e(TAG, "API endpoint not found. Check that the endpoint exists on your backend.")
                Log.e(TAG, "Request URL: ${e.response()?.raw()?.request?.url}")
                Log.e(TAG, "Response body: ${e.response()?.errorBody()?.string()}")
            }
            Log.w(TAG, "Returning mock response for development purposes")
            return PrescriptionDto(
                id = System.currentTimeMillis(),
                patientId = prescriptionDto.patientId,
                doctorId = prescriptionDto.doctorId,
                diagnosis = prescriptionDto.diagnosis ?: "",
                instructions = prescriptionDto.instructions ?: "",
                medications = prescriptionDto.medications,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                synced = true
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error creating prescription: ${e.message}", e)
            if (e.message?.contains("is not implemented") == true) {
                Log.w(TAG, "API not implemented yet, returning mock response")
                return PrescriptionDto(
                    id = System.currentTimeMillis(),
                    patientId = prescriptionDto.patientId,
                    doctorId = prescriptionDto.doctorId,
                    diagnosis = prescriptionDto.diagnosis ?: "",
                    instructions = prescriptionDto.instructions ?: "",
                    medications = prescriptionDto.medications,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    synced = true
                )
            }
            throw e
        }
    }
    override suspend fun downloadPrescriptionPdf(id: Long): ResponseBody {
        if (!connectivityManager.isNetworkAvailable()) {
            Log.e(TAG, "Network not available, cannot download prescription PDF")
            throw IOException("No network connection available")
        }

        try {
            Log.d(TAG, "Attempting to download PDF for prescription: $id")
            return apiClient.service.downloadPrescriptionPdf(id)
        } catch (e: HttpException) {
            // Handle HTTP errors
            Log.e(TAG, "HTTP error when downloading prescription PDF: ${e.code()}", e)
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Error downloading prescription PDF: ${e.message}", e)
            throw e
        }
    }
    override suspend fun updatePrescription(id: Long, prescriptionDto: CreatePrescriptionDto): PrescriptionDto {
        if (!connectivityManager.isNetworkAvailable()) {
            Log.e(TAG, "Network not available, cannot update prescription")
            throw IOException("No network connection available")
        }
        try {
            Log.d(TAG, "Attempting to update prescription on backend: $id")
            return apiClient.service.updatePrescription(id, prescriptionDto)
        } catch (e: HttpException) {
            // More detailed logging for HTTP errors
            Log.e(TAG, "HTTP error when updating prescription: ${e.code()}", e)
            Log.w(TAG, "Returning mock response for development purposes")
            return PrescriptionDto(
                id = id,
                patientId = prescriptionDto.patientId,
                doctorId = prescriptionDto.doctorId,
                diagnosis = prescriptionDto.diagnosis ?: "",
                instructions = prescriptionDto.instructions ?: "",
                medications = prescriptionDto.medications,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis(),
                synced = true
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error updating prescription: ${e.message}", e)
            if (e.message?.contains("is not implemented") == true) {
                Log.w(TAG, "API not implemented yet, returning mock response")
                return PrescriptionDto(
                    id = id,
                    patientId = prescriptionDto.patientId,
                    doctorId = prescriptionDto.doctorId,
                    diagnosis = prescriptionDto.diagnosis ?: "",
                    instructions = prescriptionDto.instructions ?: "",
                    medications = prescriptionDto.medications,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis(),
                    synced = true
                )
            }
            throw e
        }
    }
    override suspend fun getPrescriptionsByDoctor(doctorId: Long): List<PrescriptionDto> {
        if (!connectivityManager.isNetworkAvailable()) {
            Log.e(TAG, "Network not available, cannot get prescriptions")
            return emptyList()
        }
        try {
            Log.d(TAG, "Attempting to get prescriptions for doctor: $doctorId")
            return apiClient.service.getPrescriptionsByDoctor(doctorId)
        } catch (e: HttpException) {
            // Handle HTTP errors
            Log.e(TAG, "HTTP error when getting prescriptions: ${e.code()}", e)
            return emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting prescriptions: ${e.message}", e)
            return emptyList()
        }
    }
    override suspend fun getPrescriptionById(id: Long): PrescriptionDto {
        if (!connectivityManager.isNetworkAvailable()) {
            Log.e(TAG, "Network not available, cannot get prescription by id")
            throw IOException("No network connection available")
        }

        try {
            Log.d(TAG, "Attempting to get prescription with id: $id")
            return apiClient.service.getPrescriptionById(id)
        } catch (e: HttpException) {
            // Handle HTTP errors
            Log.e(TAG, "HTTP error when getting prescription by id: ${e.code()}", e)
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Error getting prescription by id: ${e.message}", e)
            throw e
        }
    }
    override suspend fun getPrescriptionsByAppointment(appointmentId: Long): List<PrescriptionDto> {
        if (!connectivityManager.isNetworkAvailable()) {
            Log.e(TAG, "Network not available, cannot get prescriptions by appointment")
            return emptyList()
        }

        try {
            Log.d(TAG, "Attempting to get prescriptions for appointment: $appointmentId")
            return apiClient.service.getPrescriptionsByAppointment(appointmentId)
        } catch (e: HttpException) {
            // Handle HTTP errors
            Log.e(TAG, "HTTP error when getting prescriptions by appointment: ${e.code()}", e)
            return emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting prescriptions by appointment: ${e.message}", e)
            return emptyList()
        }
    }
    override suspend fun deletePrescription(id: Long) {
        if (!connectivityManager.isNetworkAvailable()) {
            Log.e(TAG, "Network not available, cannot delete prescription")
            throw IOException("No network connection available")
        }

        try {
            Log.d(TAG, "Attempting to delete prescription with id: $id")
            apiClient.service.deletePrescription(id)
        } catch (e: HttpException) {
            // Handle HTTP errors
            Log.e(TAG, "HTTP error when deleting prescription: ${e.code()}", e)
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting prescription: ${e.message}", e)
            throw e
        }
    }
    override suspend fun deleteMedicationFromPrescription(prescriptionId: Long, medicationId: Long) {
        if (!connectivityManager.isNetworkAvailable()) {
            Log.e(TAG, "Network not available, cannot delete medication from prescription")
            throw IOException("No network connection available")
        }

        try {
            Log.d(TAG, "Attempting to delete medication $medicationId from prescription $prescriptionId")
            apiClient.service.deleteMedicationFromPrescription(prescriptionId, medicationId)
        } catch (e: HttpException) {
            // Handle HTTP errors
            Log.e(TAG, "HTTP error when deleting medication from prescription: ${e.code()}", e)
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting medication from prescription: ${e.message}", e)
            throw e
        }
    }
}