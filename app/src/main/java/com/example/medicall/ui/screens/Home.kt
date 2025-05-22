package com.example.medicall.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.app.components.Navbar
import com.example.medicall.repository.RepositoryHolder
import com.example.medicall.service.UserService
import com.example.medicall.ui.components.Header
import com.example.medicall.ui.components.DoctorsList
import com.example.medicall.viewmodel.DoctorModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun Home(navController: NavController, doctorModel: DoctorModel, userId: String) {
    var selectedItem by remember { mutableStateOf(0) }
    var userName by remember { mutableStateOf("Loading...") }
    val context = LocalContext.current

    val userService = remember { UserService.createInstance() }

    LaunchedEffect(Unit){
        doctorModel.getDoctors()
    }

    LaunchedEffect(userId) {
        userService.getUserById(userId).enqueue(object : Callback<com.example.medicall.service.UserResponse> {
            override fun onResponse(
                call: Call<com.example.medicall.service.UserResponse>,
                response: Response<com.example.medicall.service.UserResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!
                    userName = user.first_name
                } else {
                    Toast.makeText(context, "Erreur serveur", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<com.example.medicall.service.UserResponse>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(context, "Erreur réseau", Toast.LENGTH_SHORT).show()
            }
        })
    }

    Scaffold(
        bottomBar = {
            Navbar(navController, selectedItem) { newIndex ->
                selectedItem = newIndex
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Header(userName = userName, context)

            DoctorsList(doctorModel = doctorModel, navController = navController)
        }
    }
}