package com.example.medicall.repository

import Appointment
import BookAppointmentRequest
import BookResponse
import TimeSlot
import com.example.medicall.service.Endpoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface AppointmentRepository {
    suspend fun getDoctorAvailableSlots(doctorId: Int, date: String): Result<List<TimeSlot>>
    suspend fun bookAppointment(request: BookAppointmentRequest): Result<BookResponse>
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
}