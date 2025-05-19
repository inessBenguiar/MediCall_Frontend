package com.example.medicall.viewmodel

import BookAppointmentRequest
import TimeSlot
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun fetchAvailableSlots(doctorId: Int, date: String) {
        viewModelScope.launch {
            val result = appointmentRepository.getDoctorAvailableSlots(doctorId, date)

            result.onSuccess { slots ->
                _availableSlots.value = slots
            }.onFailure { exception ->
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
}