package com.example.medicall.service


data class SignupResponse(
    val message: String,
    val user: User
)

data class User(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val role: String
)
