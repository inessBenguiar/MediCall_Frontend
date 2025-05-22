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

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

   private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.242.216:3000/")
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: PrescriptionApiService = retrofit.create(PrescriptionApiService::class.java)

    init {
        Log.d(TAG, "ApiClient initialized with base URL: ${retrofit.baseUrl()}")
    }
}