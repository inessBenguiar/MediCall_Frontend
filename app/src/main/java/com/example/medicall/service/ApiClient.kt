package com.example.medicall.service



import android.content.Context
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient(context: Context) {
    private val TAG = "ApiClient"

    // Create an HTTP client with logging
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // Create Retrofit instance with logging
    // FIXED: Updated base URL to match NestJS controller path
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.39.216:3000/")
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Create API service
    val service: PrescriptionApiService = retrofit.create(PrescriptionApiService::class.java)

    init {
        Log.d(TAG, "ApiClient initialized with base URL: ${retrofit.baseUrl()}")
    }
}