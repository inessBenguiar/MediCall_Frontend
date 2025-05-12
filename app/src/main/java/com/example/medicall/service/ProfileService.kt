package com.example.medicall.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import com.example.baseUrl
import retrofit2.Call

data class UserResponse(
    val first_name: String,
    val family_name: String,
    val role: String,
    val email:String,
    val phone:String,
    val address:String
)

interface UserService {
    @GET("users/{id}")
    fun getUserById(@Path("id") id: String): Call<UserResponse>

    companion object {
        private var INSTANCE: UserService? = null
        fun createInstance(): UserService {
            if (INSTANCE == null) {
                INSTANCE = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(UserService::class.java)
            }
            return INSTANCE!!
        }
    }
}
