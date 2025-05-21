package com.example.medicall.service


import BookAppointmentRequest
import BookResponse
import TimeSlot
import com.example.baseUrl
import com.example.medicall.entity.ClinicResponse
import com.example.medicall.entity.Doctor
import com.example.medicall.entity.DoctorResponse
import com.example.medicall.ui.components.WorkingDay
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface Endpoint {

    @GET("doctors")
    suspend fun getDoctors(): List<Doctor>

    /**
     * Get doctor profile information
     */
    @GET("doctors/{doctorId}")
    suspend fun getDoctorProfile(@Path("doctorId") doctorId: Long): Response<Doctor>

    @GET("doctors/{doctorId}/working-days")
    suspend fun getDoctorWorkingDays(@Path("doctorId") doctorId: Long): Response<List<WorkingDay>>

    @Multipart
    @POST("doctors/update")
    suspend fun updateDoctorProfile(
        @Part("data") data: RequestBody,
        @Part photo: MultipartBody.Part? = null
    ): Response<DoctorResponse>

    @Multipart
    @POST("doctors/{doctorId}/photo")
    suspend fun uploadDoctorPhoto(
        @Path("doctorId") doctorId: Long,
        @Part photo: MultipartBody.Part
    ): Response<PhotoUploadResponse>

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


