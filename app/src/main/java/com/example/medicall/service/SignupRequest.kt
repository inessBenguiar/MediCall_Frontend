package com.example.medicall.service


data class SignupRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val role: String // "patient" ou "doctor"
)
