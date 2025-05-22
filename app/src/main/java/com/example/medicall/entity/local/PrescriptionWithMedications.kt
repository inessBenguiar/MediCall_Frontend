package com.example.medicall.entity.local

import androidx.room.Embedded
import androidx.room.Relation
import com.example.medicall.entity.Medication
import com.example.medicall.entity.Prescription

data class PrescriptionWithMedications(
    @Embedded val prescription: Prescription,
    @Relation(
        parentColumn = "id",
        entityColumn = "prescriptionId"
    )
    val medications: List<Medication>
)

