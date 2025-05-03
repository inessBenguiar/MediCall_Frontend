package com.example.medicall.service

import retrofit2.http.Body
import retrofit2.http.POST
import com.example.baseUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call



interface AuthService {
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("auth/signup")
    fun signup(@Body request: SignupRequest): Call<SignupResponse>
    companion object {
        private var INSTANCE: AuthService? = null
        fun createInstance(): AuthService {
            if (INSTANCE == null) {
                INSTANCE = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(AuthService::class.java)
            }
            return INSTANCE!!
        }
    }
}
