package com.example.medicall.repository



import com.example.medicall.entity.LocalPrescription
import com.example.medicall.entity.Medication
import com.example.medicall.entity.Prescription
import com.example.medicall.entity.local.MedicationDao
import com.example.medicall.entity.local.PrescriptionDao
import com.example.medicall.entity.local.PrescriptionWithMedications
import kotlinx.coroutines.flow.Flow

class PrescriptionRepository(
    private val prescriptionDao: PrescriptionDao,
    private val medicationDao: MedicationDao
) {
    fun getByDoctor(doctorId: String): Flow<List<PrescriptionWithMedications>> {
        // Convert doctorId from String to Long to match the function signature in PrescriptionDao
        return prescriptionDao.getByDoctorWithMedications(doctorId.toLong())
    }

    suspend fun getById(id: Long): PrescriptionWithMedications? {
        val prescription = prescriptionDao.getById(id) ?: return null
        val medications = medicationDao.getByPrescription(id)
        return PrescriptionWithMedications(prescription, medications)
    }

    suspend fun insert(prescription: Prescription, medications: List<Medication>): Long {
        val id = prescriptionDao.insert(prescription)
        medications.forEach { medication ->
            medicationDao.insert(medication.copy(prescriptionId = id))
        }
        return id
    }

    suspend fun update(prescription: Prescription, medications: List<Medication>) {
        prescriptionDao.update(prescription)
        // Delete old medications
        medicationDao.deleteByPrescription(prescription.id)
        // Add new medications
        medications.forEach { medication ->
            medicationDao.insert(medication.copy(prescriptionId = prescription.id))
        }
    }

    suspend fun delete(prescription: Prescription) {
        // First delete associated medications
        medicationDao.deleteByPrescription(prescription.id)
        // Then delete the prescription
        prescriptionDao.delete(prescription)
    }

    suspend fun getUnsyncedPrescriptions(): List<LocalPrescription> {
        val unsyncedPrescriptions = prescriptionDao.getUnsyncedPrescriptions()
        return unsyncedPrescriptions.map { prescWithMeds ->
            LocalPrescription(
                id = prescWithMeds.prescription.id,
                patientId = getPatientIdFromName(prescWithMeds.prescription.patientName),
                doctorId = prescWithMeds.prescription.doctorId,
                diagnosis = prescWithMeds.prescription.diagnosis,
                instructions = prescWithMeds.prescription.instructions,
                medications = prescWithMeds.medications
            )
        }
    }

    // Make this function public so it can be used by the ViewModel
    fun getPatientIdFromName(patientName: String): Long {
        // In a real app, this would query a patient database
        // This is just a placeholder
        return 1L
    }

    suspend fun updateRemoteId(localId: Long, remoteId: Long) {
        prescriptionDao.updateRemoteId(localId, remoteId)
    }

    suspend fun markAsUnsynced(id: Long) {
        prescriptionDao.markAsUnsynced(id)
    }

    suspend fun markAsSynced(localId: Long, remoteId: Long) {
        prescriptionDao.updateRemoteId(localId, remoteId)
        prescriptionDao.markAsSynced(localId)
    }
}