package com.example.medicall.ui.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.medicall.ui.dao.PrescriptionsDao
import com.example.medicall.ui.entities.Prescriptions

@Database(entities = [Prescriptions::class],version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getPrescriptionsDo(): PrescriptionsDao
}