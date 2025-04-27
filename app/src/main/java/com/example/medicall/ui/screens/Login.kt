package com.example.medicall.ui.screens


import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.medicall.ui.components.LoginForm
import com.example.medicall.ui.viewModel.AuthModel

@Composable
fun Login(authModel: AuthModel, navController: NavController) {
    LoginForm(authModel, navController)
}

