package com.example.medicall

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.medicall.ui.screens.Home
import com.example.medicall.ui.screens.Login
import com.example.medicall.ui.screens.Register
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Home()
            //Login()
          Register()
        }
    }
}
