package com.example.medicall.di


import android.content.Context
import androidx.room.Room
import com.example.medicall.entity.local.AppDatabase
import com.example.medicall.repository.PrescriptionRepository
import com.example.medicall.repository.RemotePrescriptionRepository
import com.example.medicall.repository.RemotePrescriptionRepositoryImpl
import com.example.medicall.service.ApiClient
import com.example.medicall.service.PrescriptionApiService
import com.example.medicall.service.PrescriptionApiServiceImpl
import com.example.medicall.sync.PrescriptionSyncManager
import com.example.medicall.util.ConnectivityManager
import com.example.medicall.viewmodel.PrescriptionViewModel

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Database - Use the same instance from the companion object
    single { AppDatabase.getDatabase(androidContext()) }

    // DAOs
    single { get<AppDatabase>().prescriptionDao() }
    single { get<AppDatabase>().medicationDao() }

    // Repository
    single {
        PrescriptionRepository(
            prescriptionDao = get(),
            medicationDao = get()
        )
    }

    // Network
    single { ConnectivityManager(androidContext()) }
    single { ApiClient(androidContext()) }

    // API Service with proper implementation
    single<PrescriptionApiService> {
        PrescriptionApiServiceImpl(
            apiClient = get(),
            connectivityManager = get()
        )
    }

    // Remote Repository
    single<RemotePrescriptionRepository> {
        RemotePrescriptionRepositoryImpl(
            apiService = get()
        )
    }

    // Sync manager
    single {
        PrescriptionSyncManager(
            context = androidContext(),
            localRepository = get(),
            remoteRepository = get(),
            connectivityManager = get()
        )
    }

    // Preferences


    // ViewModel
    viewModel { params ->
        PrescriptionViewModel(
            localRepository = get(),
            doctorId = params.get(),
            apiService = get(),
            syncManager = get(),
            connectivityManager = get()
        )
    }
}