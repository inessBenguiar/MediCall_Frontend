package com.example.medicall.repository

import com.example.medicall.service.Endpoint

class DoctorRepository(private val endpoint: Endpoint) {
    suspend fun getDoctors() = endpoint.getDoctors();
}