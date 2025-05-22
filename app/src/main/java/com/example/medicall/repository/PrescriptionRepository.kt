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
        medicationDao.deleteByPrescription(prescription.id)
       medications.forEach { medication ->
            medicationDao.insert(medication.copy(prescriptionId = prescription.id))
        }
    }

    suspend fun delete(prescription: Prescription) {
        medicationDao.deleteByPrescription(prescription.id)
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

    fun getPatientIdFromName(patientName: String): Long {

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