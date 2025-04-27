package com.example.medicall.ui.entities

import com.google.gson.annotations.SerializedName


data class Users(
    @SerializedName("user_id")
    val id: Int,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("familly_name")
    val familyName: String,
    val email: String,
    val phone: String,
    val address: String,
    val role: String,
    @SerializedName("created_at")
    val created: String
)