package com.example.medicall.repository

import android.util.Log
import com.example.medicall.entity.CreatePrescriptionDto
import com.example.medicall.entity.PrescriptionDto
import com.example.medicall.service.PrescriptionApiService

class RemotePrescriptionRepositoryImpl(
    private val apiService: PrescriptionApiService
) : RemotePrescriptionRepository {
    private val TAG = "RemotePrescriptionRepo"

    override suspend fun createPrescription(prescription: CreatePrescriptionDto): PrescriptionDto {
        try {
            Log.d(TAG, "Attempting to create prescription on remote API")
            Log.d(TAG, "Prescription data: appointment_id=${prescription.appointment_id}, patientId=${prescription.patientId}, doctorId=${prescription.doctorId}")

            val result = apiService.createPrescription(prescription)
            Log.d(TAG, "Successfully created prescription with remote ID: ${result.id}")
            return result
        } catch (e: Exception) {
            Log.e(TAG, "Error creating prescription on remote API: ${e.message}", e)
            throw e
        }
    }

    override suspend fun updatePrescription(
        id: Long,
        prescription: CreatePrescriptionDto
    ): PrescriptionDto {
        try {
            Log.d(TAG, "Attempting to update prescription $id on remote API")
            val result = apiService.updatePrescription(id, prescription)
            Log.d(TAG, "Successfully updated prescription with remote ID: ${result.id}")
            return result
        } catch (e: Exception) {
            Log.e(TAG, "Error updating prescription on remote API: ${e.message}", e)
            throw e
        }
    }

    override suspend fun deletePrescription(id: Long) {
        try {
            Log.d(TAG, "Attempting to delete prescription $id from remote API")
            apiService.deletePrescription(id)
            Log.d(TAG, "Successfully deleted prescription with ID: $id")
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting prescription from remote API: ${e.message}", e)
            throw e
        }
    }}