package com.example.medicall.service



import com.example.baseUrl
import com.example.medicall.entity.Doctor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface Endpoint {

    @GET("doctors")
    suspend fun getDoctors(): List<Doctor>



    companion object {
        private var INSTANCE: Endpoint? = null
        fun createInstance(): Endpoint {
            if(INSTANCE ==null) {
                INSTANCE = Retrofit.Builder().baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().
                    create(Endpoint::class.java)
            }
            return INSTANCE!!
        }
    }

}


