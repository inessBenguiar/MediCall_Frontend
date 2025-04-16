package com.example.medicall.ui.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="prescriptions" )
data class Prescriptions(
    @PrimaryKey
    var id:String,
    var firstName:String?,
    var lastName:String?
)