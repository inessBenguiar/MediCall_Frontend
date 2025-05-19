package com.example.medicall.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.app.components.Navbar
import com.example.medicall.ui.Navigation.Screens
import com.example.medicall.ui.components.Header
import com.example.medicall.ui.preferences.deleteId
import com.example.medicall.R
import com.example.medicall.repository.RepositoryHolder
import com.example.medicall.ui.components.DoctorsList
import com.example.medicall.viewmodel.DoctorModel


@Composable
fun Home(navController: NavController) {

    var selectedItem by remember { mutableStateOf(0) }

    val doctorModel = remember { DoctorModel(RepositoryHolder.DoctorRepository) }

    Scaffold(
        bottomBar = {

            Navbar(selectedItem) { newIndex ->
                selectedItem = newIndex
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val context = LocalContext.current
            Header(userName = "Bouchra")

            IconButton(onClick = { deleteId(context)
                navController.navigate(Screens.MainScreen.route){popUpTo(0)}
               }) {Icon(
                imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "Logout"
            )
            }

            //DoctorsList(navController = navController)

        }
    }
}
