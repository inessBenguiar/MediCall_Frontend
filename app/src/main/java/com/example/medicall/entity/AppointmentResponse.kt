package com.example.medicall.entity

@kotlinx.serialization.Serializable
data class AppointmentResponse(
    val id: String,
    val doctorName: String,
    val specialty: String,
    val clinic: String,
    val location: String,
    val date: String,
    val time: String,
    val doctorImageUrl: String? = null  // If your backend provides image URLs
)
