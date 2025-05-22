package com.example.medicall.entity

import java.util.Date
data class PrescriptionDto(
    val id: Long,
    val patientId: Long,
    val doctorId: Long,
    val diagnosis: String,
    val instructions: String,
    val createdAt: Long,
    val updatedAt: Long,
    val medications: List<MedicationDto>,
    val  appointment_id: Long? = 2L,
    val synced: Boolean
)