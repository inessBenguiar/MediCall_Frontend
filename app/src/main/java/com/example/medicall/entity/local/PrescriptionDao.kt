package com.example.medicall.entity.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.medicall.entity.Prescription
import kotlinx.coroutines.flow.Flow

@Dao
interface PrescriptionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(prescription: Prescription): Long
    @Update
    suspend fun update(prescription: Prescription)
    @Delete
    suspend fun delete(prescription: Prescription)
    @Query("SELECT * FROM prescriptions WHERE id = :id")
    suspend fun getById(id: Long): Prescription?
    @Transaction
    @Query("SELECT * FROM prescriptions WHERE doctorId = :doctorId")
    fun getByDoctorWithMedications(doctorId: Long): Flow<List<PrescriptionWithMedications>>
    @Transaction
    @Query("SELECT * FROM prescriptions WHERE isSynced = 0")
    suspend fun getUnsyncedPrescriptions(): List<PrescriptionWithMedications>
    @Query("UPDATE prescriptions SET remoteId = :remoteId WHERE id = :localId")
    suspend fun updateRemoteId(localId: Long, remoteId: Long)
    @Query("UPDATE prescriptions SET isSynced = 0 WHERE id = :id")
    suspend fun markAsUnsynced(id: Long)
    @Query("UPDATE prescriptions SET isSynced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: Long)
}