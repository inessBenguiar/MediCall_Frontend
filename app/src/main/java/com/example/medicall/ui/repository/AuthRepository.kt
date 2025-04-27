package com.example.medicall.ui.repository

import androidx.compose.runtime.MutableState
import com.example.medicall.ui.endpoints.EndPoints

class AuthRepository(private val endPoints: EndPoints) {
    suspend fun authentication(credentials: MutableState<Map<String, String>>) = endPoints.authentication(credentials)
}