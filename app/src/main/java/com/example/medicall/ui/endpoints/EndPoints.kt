package com.example.medicall.ui.endpoints

import androidx.compose.runtime.MutableState
import com.example.medicall.url
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FieldMap
import retrofit2.http.POST

interface EndPoints {
    @POST("authentication")
    suspend fun authentication (@FieldMap data: MutableState<Map<String, String>>): String

    companion object {
        var endpoint: EndPoints? = null
        fun createInstance(): EndPoints {
            if(endpoint ==null) {

                endpoint = Retrofit.Builder().baseUrl(url). addConverterFactory(GsonConverterFactory.create()).build(). create(EndPoints::class.java)

            }
            return endpoint!!
        }

    }
}
