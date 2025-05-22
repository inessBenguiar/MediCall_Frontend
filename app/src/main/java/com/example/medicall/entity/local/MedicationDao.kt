package com.example.medicall.entity.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.medicall.entity.Medication

@Dao
interface MedicationDao {
    @Insert
    suspend fun insert(medication: Medication): Long

    @Update
    suspend fun update(medication: Medication)

    @Delete
    suspend fun delete(medication: Medication)

    @Query("SELECT * FROM medications WHERE prescriptionId = :prescriptionId")
    suspend fun getByPrescription(prescriptionId: Long): List<Medication>

    @Query("DELETE FROM medications WHERE prescriptionId = :prescriptionId")
    suspend fun deleteByPrescription(prescriptionId: Long)
}