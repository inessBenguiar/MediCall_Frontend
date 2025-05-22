package com.example.medicall.repository

import Appointment
import BookAppointmentRequest
import BookResponse
import TimeSlot
import android.util.Log
import com.example.medicall.service.Endpoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.medicall.entity.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URL
import com.example.medicall.*

interface AppointmentRepository {
    suspend fun getDoctorAvailableSlots(doctorId: Int, date: String): Result<List<TimeSlot>>
    suspend fun bookAppointment(request: BookAppointmentRequest): Result<BookResponse>
    suspend fun getConfirmedAppointmentsForPatient(patientId: Int): Result<List<AppointmentResponse>>

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


    override suspend fun getConfirmedAppointmentsForPatient(patientId: Int): Result<List<AppointmentResponse>> {
        return try {
            Log.d("Repository", "Fetching confirmed appointments for patientId: $patientId")
            val response = apiService.getConfirmedAppointmentsForPatient(patientId)
            Log.d("Repository", "Response code: ${response.code()}")
            if (response.isSuccessful) {
                val body = response.body() ?: emptyList()
                Log.d("Repository", "Received ${body.size} appointments")
                Result.success(body)
            } else {
                Log.e("Repository", "API error: ${response.code()} ${response.message()}")
                Result.failure(Exception("Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("Repository", "Exception occurred", e)
            Result.failure(e)
        }
    }



}
