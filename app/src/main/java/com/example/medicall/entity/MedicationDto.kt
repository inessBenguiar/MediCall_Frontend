package com.example.medicall.entity

data class MedicationDto(
    val name: String,
    val dosage: String,
    val frequency: String,
    val instructions: String? = null,
    val duration: String? = null
)