package com.example.medicall.service


import com.example.medicall.entity.CreatePrescriptionDto
import com.example.medicall.entity.PrescriptionDto
import okhttp3.ResponseBody
import retrofit2.http.*
interface PrescriptionApiService {
    @GET("prescriptions")
    suspend fun getAllPrescriptions(): List<PrescriptionDto>
    @GET("prescriptions/{id}")
    suspend fun getPrescriptionById(@Path("id") id: Long): PrescriptionDto
    @GET("prescriptions/appointment/{appointmentId}")
    suspend fun getPrescriptionsByAppointment(@Path("appointmentId") appointmentId: Long): List<PrescriptionDto>
    @GET("prescriptions/doctor/{doctorId}")
    suspend fun getPrescriptionsByDoctor(@Path("doctorId") doctorId: Long): List<PrescriptionDto>
    @POST("prescriptions")
    suspend fun createPrescription(@Body prescriptionDto: CreatePrescriptionDto): PrescriptionDto
    @PUT("prescriptions/{id}")
    suspend fun updatePrescription(
        @Path("id") id: Long,
        @Body prescriptionDto: CreatePrescriptionDto
    ): PrescriptionDto
    @DELETE("prescriptions/{id}")
    suspend fun deletePrescription(@Path("id") id: Long)
    @DELETE("prescriptions/{prescriptionId}/medications/{medicationId}")
    suspend fun deleteMedicationFromPrescription(
        @Path("prescriptionId") prescriptionId: Long,
        @Path("medicationId") medicationId: Long
    )
    @GET("prescriptions/{id}/pdf")
    @Streaming
    suspend fun downloadPrescriptionPdf(@Path("id") id: Long): ResponseBody
}