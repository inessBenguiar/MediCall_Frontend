package com.example.medicall.entity.local


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.medicall.entity.Medication
import com.example.medicall.entity.Prescription

@Database(
    entities = [Prescription::class, Medication::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun prescriptionDao(): PrescriptionDao
    abstract fun medicationDao(): MedicationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Define the migration from version 1 to version 2
        private val MIGRATION_1_2 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // If you added new columns or tables, define the SQL to modify the schema
                // For example:
                // database.execSQL("ALTER TABLE prescriptions ADD COLUMN patientAge INTEGER")

                // If you don't remember the exact changes, you can use an empty migration
                // which will keep existing data but apply the new schema
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "prescriptions_database"
                )

                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}