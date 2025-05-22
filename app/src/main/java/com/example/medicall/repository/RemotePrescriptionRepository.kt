package com.example.medicall.repository

import com.example.medicall.entity.CreatePrescriptionDto
import com.example.medicall.entity.PrescriptionDto

interface RemotePrescriptionRepository {
    suspend fun createPrescription(prescription: CreatePrescriptionDto): PrescriptionDto
    suspend fun updatePrescription(id: Long, prescription: CreatePrescriptionDto): PrescriptionDto
    suspend fun deletePrescription(id: Long)}