package com.example.medicall.entity

data class CreatePrescriptionDto(
    val patientId: Long,
    val doctorId: Long,
    val appointment_id: Long? = 2L,
    val name: String? = null,
    val diagnosis: String = "",
    val instructions: String = "",
    val dosage: String? = null,
    val frequency: String? = null,
    val duration: String? = null,
    val medications: List<MedicationDto>
)