package com.example.medicall.repository

import Appointment
import BookAppointmentRequest
import BookResponse
import TimeSlot
import com.example.medicall.entity.ConfirmedAppointment
import com.example.medicall.service.Endpoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface AppointmentRepository {
    suspend fun getDoctorAvailableSlots(doctorId: Int, date: String): Result<List<TimeSlot>>
    suspend fun bookAppointment(request: BookAppointmentRequest): Result<BookResponse>

    // New function to get confirmed appointments
    suspend fun getConfirmedAppointments(patientId: Int): Result<List<ConfirmedAppointment>>
    suspend fun getAppointmentById(id: Int): Result<ConfirmedAppointment>
}

class AppointmentRepositoryImpl(
) : AppointmentRepository {
    private val apiService = Endpoint.createInstance()

    override suspend fun getDoctorAvailableSlots(doctorId: Int, date: String): Result<List<TimeSlot>> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.getDoctorAvailableSlots(doctorId, date)
                if (response.isSuccessful) {
                    Result.success(response.body() ?: emptyList())
                } else {
                    Result.failure(Exception("Failed to fetch available slots: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun bookAppointment(request: BookAppointmentRequest): Result<BookResponse> =
        withContext(Dispatchers.IO) {
            try {
                val response = apiService.bookAppointment(request)
                if (response.isSuccessful) {
                    response.body()?.let {
                        Result.success(it)
                    } ?: Result.failure(Exception(response.message()))
                } else {
                    Result.failure(Exception("Failed to book appointment: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun getConfirmedAppointments(patientId: Int): Result<List<ConfirmedAppointment>> =
        withContext(Dispatchers.IO) {
            try {
                val appointments = apiService.getConfirmedAppointments(patientId)
                Result.success(appointments)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    override suspend fun getAppointmentById(id: Int): Result<ConfirmedAppointment> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getAppointmentById(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@withContext Result.success(it) // <-- return here
                } ?: return@withContext Result.failure(Exception("Empty response body"))
            } else {
                return@withContext Result.failure(Exception("Failed to fetch appointment: ${response.message()}"))
            }
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }

}
