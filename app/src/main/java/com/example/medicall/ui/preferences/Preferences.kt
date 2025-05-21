package com.example.medicall.ui.preferences

import android.content.Context

//Function to save user_id
    fun saveId(context: Context, user_id: String, role: String) {

        val pref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        with(pref.edit()) {
            putString("user_id", user_id)
            putString("role", role)
            apply()
        }
    }

//Function to get user_id
    fun readId(context: Context):String? {
        val pref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val user_id = pref.getString("user_id", null)
        return user_id;
    }

    fun readRole(context: Context):String? {
    val pref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
    val role = pref.getString("role", null)
    return role;
}

//Function to delete the user_id field
    fun deleteId(context: Context) {
        val pref = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        with(pref.edit()) {
            remove("user_id")
            remove("role")
            apply()
    }
}