package com.example.medicall.ui.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.medicall.ui.entities.Prescriptions

@Dao
interface PrescriptionsDao {
    @Query("select * from prescriptions where id = :id")
    fun getUsersById(id:String):List<Prescriptions>
    @Insert
    suspend fun addUsers(vararg users:Prescriptions)
    @Update
    suspend fun updateUser(user:Prescriptions)
    @Delete
    suspend fun deleteUser(user:Prescriptions) }