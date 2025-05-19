package com.example.medicall.ui.preferences

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.medicall.entity.User


fun saveId(context: Context, email: String, pseudo: String ) {

        val pref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        with(pref.edit()) {
            putString("email", email)
            putString("pwd", pseudo)
            apply()
        }
    }


    fun readId(context: Context): User {
        val pref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val email = pref.getString("email", null)
        val pwd = pref.getString("pwd", null)
        return User(email.toString(), pwd.toString())
    }

fun deleteId(context: Context) {
    val pref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
    with(pref.edit()) {
        remove("email")
        remove("pwd")
        apply()
    }
}