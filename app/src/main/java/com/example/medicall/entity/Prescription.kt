package com.example.medicall.entity


data class Prescription(
    val appointementId: Int,
    val diagnosis: String,
    val instructions: String
)