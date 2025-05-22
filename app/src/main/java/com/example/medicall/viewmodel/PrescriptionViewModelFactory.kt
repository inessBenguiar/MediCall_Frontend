package com.example.medicall.viewmodel

import com.example.medicall.repository.PrescriptionRepository
import com.example.medicall.service.PrescriptionApiService
import com.example.medicall.sync.PrescriptionSyncManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.medicall.util.ConnectivityManager

class PrescriptionViewModelFactory(
    private val repository: PrescriptionRepository,
    private val doctorId: Long,
    private val apiService: PrescriptionApiService? = null,
    private val syncManager: PrescriptionSyncManager? = null,
    private val connectivityManager: ConnectivityManager? = null
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PrescriptionViewModel::class.java)) {
            return PrescriptionViewModel(
                localRepository = repository,
                doctorId = doctorId,
                apiService = apiService,
                syncManager = syncManager,
                connectivityManager = connectivityManager
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}