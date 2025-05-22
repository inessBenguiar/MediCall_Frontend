package com.example.medicall.entity

data class ConfirmedAppointment(
    val id: Int,
    val doctor_id: Int,
    val patient_id: Int,
    val date_time: String,
    val status: String,
    val qr_code: String?,
    val created_at: String,
    val first_name: String?,
    val family_name: String?,
    val phone: String?
)
