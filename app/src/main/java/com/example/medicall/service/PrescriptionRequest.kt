package com.example.medicall.service

data class PrescriptionRequest(
    val appointment_id: Int,
    val diagnosis: String,
    val instructions: String
)
