package com.example.medicall.entity

import com.google.gson.annotations.SerializedName
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "doctor",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Doctor(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val user_id: Long,
    val family_name: String,
    val first_name: String,
    val specialty: String,
    @SerializedName("photo")
    val photo: String,
    val contact: String,
    val experience: Int,
    val availability: String,
    val clinic: String
)
