package com.example.medicall.service


import BookAppointmentRequest
import BookResponse
import TimeSlot
import com.example.baseUrl
import com.example.medicall.entity.ClinicResponse
import com.example.medicall.entity.Doctor
import com.example.medicall.entity.DoctorResponse
import com.example.medicall.ui.components.UserInfo
import com.example.medicall.ui.components.WorkingDay
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface Endpoint {

    @GET("doctors")
    suspend fun getDoctors(): List<Doctor>

    /**
     * Get doctor profile information
     */
    @GET("doctors/{doctorId}")
    suspend fun getDoctorProfile(@Path("doctorId") doctorId: Long): Response<Doctor>

    /**
     * Update doctor profile information
     */
    @POST("doctors/update")
    suspend fun updateDoctorProfile(
        @Body request: UserInfo
    ): Response<DoctorResponse>

    @GET("/clinics/search")
    suspend fun searchClinics(
        @Query("query") query: String,
        @Query("limit") limit: Int = 5
    ): List<ClinicResponse>


    /**
     * Get doctor working days
     */
    @GET("doctors/{doctorId}/working-days")
    suspend fun getDoctorWorkingDays(
        @Path("doctorId") doctorId: Long
    ): Response<List<WorkingDay>>

    /**
     * Update doctor working days
     */
    @PUT("doctors/{doctorId}/working-days")
    suspend fun updateDoctorWorkingDays(
        @Path("doctorId") doctorId: Long,
        @Body workingDays: List<WorkingDay>
    ): Response<Void>

    //Endpoints for Booking
    @GET("doctors/{id}/{date}/available-slots")
    suspend fun getDoctorAvailableSlots(
        @Path("id") doctorId: Int,
        @Path("date") date: String
    ): Response<List<TimeSlot>>

    @POST("appointments/book")
    suspend fun bookAppointment(
        @Body bookAppointmentRequest: BookAppointmentRequest
    ): Response<BookResponse>


    companion object {
        private var INSTANCE: Endpoint? = null
        fun createInstance(): Endpoint {
            if (INSTANCE == null) {
                INSTANCE = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(Endpoint::class.java)
            }
            return INSTANCE!!
        }
    }
}
