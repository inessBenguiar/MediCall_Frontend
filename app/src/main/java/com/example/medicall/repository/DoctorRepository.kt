package com.example.medicall.repository

import com.example.medicall.service.Endpoint
import com.example.medicall.entity.Doctor
import com.example.medicall.entity.DoctorResponse
import com.example.medicall.service.ApiResult
import com.example.medicall.service.DoctorUpdateRequest
import com.example.medicall.service.WorkingDaysUpdateRequest
import com.example.medicall.ui.components.WorkingDay
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException


class DoctorRepository(private val endpoint: Endpoint) {

    suspend fun getDoctors() = endpoint.getDoctors();

    /**
     * Get doctor profile information
     */
    fun getDoctorProfile(doctorId: Long): Flow<ApiResult<Doctor>> = flow {
        emit(ApiResult.Loading)
        try {
            val response = endpoint.getDoctorProfile(doctorId)
            if (response.isSuccessful && response.body() != null) {
                emit(ApiResult.Success(response.body()!!))
            } else {
                emit(ApiResult.Error("Failed to fetch doctor profile: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("An error occurred: ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Get doctor working days
     */
    fun getWorkingDays(doctorId: Long): Flow<ApiResult<List<WorkingDay>>> = flow {
        emit(ApiResult.Loading)
        try {
            val response = endpoint.getDoctorWorkingDays(doctorId)
            if (response.isSuccessful && response.body() != null) {
                emit(ApiResult.Success(response.body()!!))
            } else {
                emit(ApiResult.Error("Failed to fetch working days: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("An error occurred: ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO)

    fun updateDoctorProfile(
        userId: Int,
        specialty: String,
        experience: Int,
        contact: String,
        clinicName: String,
        clinicAddress: String,
        clinicMap: String,
        breakstart: String,
        breakend: String,
        startworkTime: String?,
        endworkTime: String?,
        facebook: String?,
        instagram: String?,
        linkedin: String?,
        workOnWeekend: Boolean,
        workEveryDay: Boolean,
        workingDays: List<WorkingDay>,
        photoBytes: ByteArray? // Pass photo directly here
    ): Flow<ApiResult<DoctorResponse>> = flow {
        emit(ApiResult.Loading)
        try {

            val doctorUpdateRequest = DoctorUpdateRequest(
                userId = userId,
                specialty = specialty,
                experience = experience,
                contact = contact,
                clinicAddress = clinicAddress,
                clinicName = clinicName,
                clinicMap = clinicMap,
                breakstart = breakstart,
                breakend = breakend,
                startworkTime = startworkTime,
                endworkTime = endworkTime,
                facebook = facebook,
                instagram = instagram,
                linkedin = linkedin,
                workingDays = workingDays,
                workOnWeekend = workOnWeekend,
                workEveryDay = workEveryDay
            )
            println("Prepare to call Endpoint....")
            val jsonString = Gson().toJson(doctorUpdateRequest )
            val dataPart = jsonString.toRequestBody("application/json".toMediaTypeOrNull())
            println(doctorUpdateRequest.workingDays)
            val photoPart = photoBytes?.let {
                MultipartBody.Part.createFormData(
                    "photo",
                    "doctor_${System.currentTimeMillis()}.jpg",
                    it.toRequestBody("image/jpeg".toMediaTypeOrNull())
                )
            }
            val response = withContext(Dispatchers.IO) {
                endpoint.updateDoctorProfile(dataPart, photoPart)
            }
            println("Endpoint called, response: ${response.code()}")

            if (response.isSuccessful && response.body() != null) {
                emit(ApiResult.Success(response.body()!!))
            } else {
                emit(ApiResult.Error("Failed to update profile: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("An error occurred: ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO)
}
