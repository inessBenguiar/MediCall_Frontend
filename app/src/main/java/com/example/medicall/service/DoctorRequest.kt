package com.example.medicall.service

import com.example.medicall.ui.components.WorkingDay

data class DoctorUpdateRequest(
    val userId: Int,
    val specialty: String,
    val experience: Int,
    val contact: String,
    val clinicAddress: String,
    val clinicName: String,
    val clinicMap: String,
    val breakstart: String,
    val breakend: String,
    val startworkTime: String?,
    val endworkTime: String?,
    val facebook: String?,
    val instagram: String?,
    val linkedin: String?,
    val workingDays: List<WorkingDay>,
    val workOnWeekend: Boolean,
    val workEveryDay: Boolean
)

data class WorkingDaysUpdateRequest(
    val workingDays: List<WorkingDay>
)

data class PhotoUploadResponse(
    val photoUrl: String
)