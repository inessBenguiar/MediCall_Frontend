package com.example.medicall.service

data class LoginRequest(
    val email: String,
    val password: String,
    val fcm_token: String? = null
)
