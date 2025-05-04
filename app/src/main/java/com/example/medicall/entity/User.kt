package com.example.medicall.entity

import androidx.room.Entity

@Entity(tableName = "user")
data class User(
    val email: String,
    val phone: String,
    )
