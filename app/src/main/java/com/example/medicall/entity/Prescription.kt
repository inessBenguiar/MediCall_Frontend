package com.example.medicall.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "prescriptions")
data class Prescription(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val patientName: String,
    val patientAge: Int? = null,
    val doctorId: Long, // Changed from String to Long
    val date: String,
    val diagnosis: String? = null,
    val instructions: String? = null,
    val remoteId: Long? = null,
    val createdAt: Long,
    val updatedAt: Long,

    val isSynced: Boolean = false
)