package com.example.medicall.ui.repository

import com.example.medicall.ui.endpoints.EndPoints

object RepositoryHolder {
    val authRepository by lazy { AuthRepository(EndPoints.createInstance()) }
}