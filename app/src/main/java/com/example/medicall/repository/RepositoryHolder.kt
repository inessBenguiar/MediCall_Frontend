package com.example.medicall.repository


import com.example.medicall.service.Endpoint

object RepositoryHolder {
    val DoctorRepository by lazy { DoctorRepository(Endpoint.createInstance()) }
}

