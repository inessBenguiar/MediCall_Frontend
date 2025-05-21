package com.example.medicall.repository

import com.example.medicall.service.Endpoint
import android.net.Uri
import com.example.medicall.entity.Doctor
import com.example.medicall.service.ApiResult
import com.example.medicall.ui.components.UserInfo
import com.example.medicall.ui.components.WorkingDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.BitSet


class DoctorRepository(private val endpoint: Endpoint) {

    suspend fun getDoctors() = endpoint.getDoctors();

    /**
     * Gets doctor profile information from the API
     */
    fun getDoctorProfile(doctorId: Long): Flow<ApiResult<Doctor>> = flow {
        emit(ApiResult.Loading)
        try {
            val response = endpoint.getDoctorProfile(doctorId)
            if (response.isSuccessful) {
                response.body()?.let { doctor ->
                    emit(ApiResult.Success(doctor))
                } ?: emit(ApiResult.Error("Empty response body"))
            } else {
                emit(ApiResult.Error("Failed to get doctor profile: ${response.code()}"))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("Network error: ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO)


    /**
     * Updates doctor profile information
     */
    fun updateDoctorProfile(
        userInfo: UserInfo
    ): Flow<ApiResult<Boolean>> = flow {
        emit(ApiResult.Loading)
        try {
            // Convert working days to availability bit pattern
            val availability = BitSet(5).apply {
                userInfo.workingDays.forEachIndexed { index, day ->
                    set(index, day.isWorking)
                }
            }

            val response = endpoint.updateDoctorProfile(
                userInfo
            )

            if (response.isSuccessful) {
                emit(ApiResult.Success(true))
            } else {
                emit(ApiResult.Error("Failed to update profile: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("Network error: ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO)


    /**
     * Updates doctor working days schedule
     */
    fun updateWorkingDays(doctorId: Long, workingDays: List<WorkingDay>): Flow<ApiResult<Boolean>> = flow {
        emit(ApiResult.Loading)
        try {
            val response = endpoint.updateDoctorWorkingDays(doctorId, workingDays)

            if (response.isSuccessful) {
                emit(ApiResult.Success(true))
            } else {
                emit(ApiResult.Error("Failed to update working days: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("Network error: ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Gets doctor working days schedule
     */
    fun getWorkingDays(doctorId: Long): Flow<ApiResult<List<WorkingDay>>> = flow {
        emit(ApiResult.Loading)
        try {
            val response = endpoint.getDoctorWorkingDays(doctorId)

            if (response.isSuccessful) {
                response.body()?.let { workingDays ->
                    emit(ApiResult.Success(workingDays))
                } ?: emit(ApiResult.Error("Empty response body"))
            } else {
                emit(ApiResult.Error("Failed to get working days: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("Network error: ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO)
}