package com.example.medicall.service

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Query
import com.example.baseUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call



interface AuthService {
    data class UserRoleResponse(
        val id: String,
        val role: String
    )
    @GET("users/user-role")
    fun getUserRole(@Query("email") email: String): Call<UserRoleResponse>

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
