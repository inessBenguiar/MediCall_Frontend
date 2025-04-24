package com.example.medicall.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val family_name: String,
    val first_name: String,
    val address: String,
    val email: String,
    val phone: String,
    val password: String,
    val role: Role,

    )
