package com.example.medicall.entity  // or .network.response if you're organizing responses separately

data class AppointmentResponse(
    val status: String,
    val message: String,
    val data: AppointmentData
)

data class AppointmentData(
    val id: Int,
    val doctorName: String,
    val date: String,
    val time: String
)

