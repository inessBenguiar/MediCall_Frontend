package com.example.medicall.viewmodel

import BookAppointmentRequest
import TimeSlot
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicall.entity.ConfirmedAppointment
import com.example.medicall.repository.AppointmentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppointmentViewModel(
    private val appointmentRepository: AppointmentRepository
) : ViewModel() {

    private val _availableSlots = MutableStateFlow<List<TimeSlot>>(emptyList())
    val availableSlots: StateFlow<List<TimeSlot>> = _availableSlots

    private val _bookingStatus = MutableStateFlow<String?>(null)
    val bookingStatus: StateFlow<String?> = _bookingStatus

    // New: confirmed appointments
    private val _confirmedAppointments = MutableStateFlow<List<ConfirmedAppointment>>(emptyList())
    val confirmedAppointments: StateFlow<List<ConfirmedAppointment>> = _confirmedAppointments

    private val _confirmedAppointmentsError = MutableStateFlow<String?>(null)
    val confirmedAppointmentsError: StateFlow<String?> = _confirmedAppointmentsError

    fun fetchAvailableSlots(doctorId: Int, date: String) {
        viewModelScope.launch {
            val result = appointmentRepository.getDoctorAvailableSlots(doctorId, date)

            result.onSuccess { slots ->
                _availableSlots.value = slots
            }.onFailure {
                _availableSlots.value = emptyList()
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

    // New function to get confirmed appointments by patient
    fun fetchConfirmedAppointments(patientId: Int) {
        viewModelScope.launch {
            val result = appointmentRepository.getConfirmedAppointments(patientId)
            result.onSuccess { appointments ->
                _confirmedAppointments.value = appointments
                _confirmedAppointmentsError.value = null
            }.onFailure { exception ->
                _confirmedAppointments.value = emptyList()
                _confirmedAppointmentsError.value = exception.message
            }
        }


    }


    private val _appointment = MutableStateFlow<ConfirmedAppointment?>(null)
    val appointment: StateFlow<ConfirmedAppointment?> = _appointment.asStateFlow()

    private val _appointmentError = MutableStateFlow<String?>(null)
    val appointmentError: StateFlow<String?> = _appointmentError.asStateFlow()

    fun fetchAppointment(id: Int) {
        viewModelScope.launch {
            val result = appointmentRepository.getAppointmentById(id)
            result.onSuccess { appointment ->
                _appointment.value = appointment
                _appointmentError.value = null
            }.onFailure { exception ->
                _appointment.value = null
                _appointmentError.value = exception.message
            }
        }
    }
}
