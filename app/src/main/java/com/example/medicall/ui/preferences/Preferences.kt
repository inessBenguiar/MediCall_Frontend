package com.example.medicall.ui.preferences

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

class Preferences {
    @Composable
    fun saveId(value: String) {
        val context = LocalContext.current
        val pref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        with(pref.edit()) {
            putString("user_id", value)
            apply()
        }
    }

    @Composable
    fun readId(): String? {
        val context = LocalContext.current
        val pref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        return pref.getString("user_id", null)
    }
}