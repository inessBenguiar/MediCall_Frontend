package com.example.medicall.entity

import com.google.gson.annotations.SerializedName
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.medicall.ui.components.ClinicInfo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.BitSet

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
    val phone: String,
    val experience: Int,
    val address: String,
   /* val facebook: String? = null,
    val instagram: String? = null,
    val linkedin: String? = null,
    val clinic: String,
    val workOnWeekend: Boolean? = null*/
)
data class DoctorResponse(
    val status: String,
    val message: String
)
data class ClinicResponse(
    val id: Int,
    val name: String,
    val address: String,
)
