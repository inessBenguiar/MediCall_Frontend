package com.example.medicall.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicall.entity.Doctor
import com.example.medicall.repository.DoctorRepository
import kotlinx.coroutines.launch

class DoctorModel(private val repository: DoctorRepository): ViewModel() {
    val doctors = mutableStateOf(emptyList<Doctor>())
    val loading = mutableStateOf(false)
    val error = mutableStateOf(false)
    val errorMessage = mutableStateOf("")  // AJOUT ICI
    val doctor = mutableStateOf<Doctor?>(null)

    init {
        getDoctors()
    }

    fun getDoctors() {
        loading.value = true
        viewModelScope.launch {
            try {
                doctors.value = repository.getDoctors()
            } catch (e: Exception) {
                error.value = true
                errorMessage.value = "Impossible de récupérer les docteurs"
                println("Erreur récup Doctors: ${e.message}") // pour le debug log

            } finally {
                loading.value = false
            }
        }
    }
    fun navigateToDetail(
        firstName: String,
        familyName: String,
        photoUrl: String,
        address: String,
        phone: String,
        email: String
    ) {

    }

}
