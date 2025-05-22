package com.example.medicall.entity

data class LocalPrescription(
    val id: Long,
    val patientId: Long,
    val doctorId: Long,
    val diagnosis: String?,
    val instructions: String?,
    val medications: List<Medication>
)