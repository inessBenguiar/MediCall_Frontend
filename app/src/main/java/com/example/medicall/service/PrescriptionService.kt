package com.example.medicall.service

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.baseUrl

interface PrescriptionService {
    @POST("prescriptions")
    fun addPrescription(@Body request: PrescriptionRequest): Call<Void>

    companion object {
        private var INSTANCE: PrescriptionService? = null

        fun createInstance(): PrescriptionService {
            if (INSTANCE == null) {
                INSTANCE = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(PrescriptionService::class.java)
            }
            return INSTANCE!!
        }
    }
}
