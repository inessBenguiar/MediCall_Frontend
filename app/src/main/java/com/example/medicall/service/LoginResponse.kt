package com.example.medicall.service

data class LoginResponse(
    val id: String,
    val access_token: String,
    val first_name: String,
    val family_name: String,
    val role: String
)
