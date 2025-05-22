package com.example.medicall.viewmodel

import BookAppointmentRequest
import TimeSlot
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicall.entity.AppointmentResponse
import com.example.medicall.repository.AppointmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AppointmentViewModel(
    private val appointmentRepository: AppointmentRepository
) : ViewModel() {

    private val _availableSlots = MutableStateFlow<List<TimeSlot>>(emptyList())
    val availableSlots: StateFlow<List<TimeSlot>> = _availableSlots

    private val _bookingStatus = MutableStateFlow<String?>(null)
    val bookingStatus: StateFlow<String?> = _bookingStatus

    private val _appointments = MutableStateFlow<List<AppointmentResponse>>(emptyList())
    val appointments: StateFlow<List<AppointmentResponse>> = _appointments

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun fetchAvailableSlots(doctorId: Int, date: String) {
        viewModelScope.launch {
            val result = appointmentRepository.getDoctorAvailableSlots(doctorId, date)
            result.onSuccess { slots ->
                _availableSlots.value = slots
            }.onFailure {
                _availableSlots.value = emptyList()
                _errorMessage.value = "Failed to load available slots: ${it.localizedMessage}"
            }
        }
    }

    fun bookAppointment(request: BookAppointmentRequest) {
        viewModelScope.launch {
            val result = appointmentRepository.bookAppointment(request)
            result.onSuccess { appointment ->
                _bookingStatus.value = appointment.message
            }.onFailure { exception ->
                _bookingStatus.value = "Failed to book appointment: ${exception.message}"
            }
        }
    }

    private val _loading = MutableStateFlow(false)
    val loading = _loading

    fun loadAppointments(patientId: Int) {
        Log.d("ViewModel", "Loading appointments for patientId: $patientId")
        viewModelScope.launch {
            _loading.value = true
            _errorMessage.value = null
            val result = appointmentRepository.getConfirmedAppointmentsForPatient(patientId)
            result.onSuccess { list ->
                Log.d("ViewModel", "Successfully loaded ${list.size} appointments")
                _appointments.value = list
                _loading.value = false
            }.onFailure { error ->
                Log.e("ViewModel", "Failed to load appointments", error)
                _errorMessage.value = error.message
                _loading.value = false
            }
        }
    }

}
