package com.example.medicall.service

import com.example.baseUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

data class Appointment(
    val id: Int,
    val patient_id: Int,
    val first_name: String,
    val family_name: String,
    val phone: String,
    val doctor_id: Int,
    val date_time: String,
    val status: String
)

interface AppointmentService {

    @GET("appointments/by-doctor")
    suspend fun getAppointmentsByDoctor(@Query("doctorId") doctorId: Int): List<Appointment>


    @GET("appointments/{id}")
    suspend fun getAppointmentById(@Path("id") id: Int): Appointment


    companion object {
        private var INSTANCE: AppointmentService? = null

        fun createInstance(): AppointmentService {
            if (INSTANCE == null) {
                INSTANCE = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(AppointmentService::class.java)
            }
            return INSTANCE!!
        }
    }
}
