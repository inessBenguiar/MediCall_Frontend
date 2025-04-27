package com.example.medicall

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.medicall.ui.Navigation.Navigation
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

data class UserInfo(val email: String?, val pwd: String?)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            val context = LocalContext.current
            // Home()
            //Login()
          //Register()
            //DoctorDetail()
                Navigation(navController)

        }
    }
}
@Serializable
data class users(
    val id : String,
    val first_name: String,
    val family_name: String,
    val email : String,
    val role: String,
    val phone : String,
    val address: String,
    val created_at : String,
)
val supabase = createSupabaseClient(
    supabaseUrl = "https://cuqixcbntxcugjwbebry.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImN1cWl4Y2JudHhjdWdqd2JlYnJ5Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDQ1NDYwMjQsImV4cCI6MjA2MDEyMjAyNH0.gDZHbSUANicz2QrCt6LOHak98Ywwje851mK9uZ2vfwE") {
    install(Postgrest)
}
@Composable
fun UsersList() {
    val users = remember { mutableStateListOf<users>() }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val result = withContext(Dispatchers.IO) {
                supabase
                    .from("users")
                    .select()
                    .decodeList<users>()
            }
            users.addAll(result)
        } catch (e: Exception) {
            error = e.message
            Log.e("SUPABASE", "Error fetching users: ${e.message}", e)
        }
    }

    if (error != null) {
        Text("Error: $error", modifier = Modifier.padding(8.dp))
    } else {
        LazyColumn {
            items(users) { user ->
                Text(
                    text = "User ${user.id} (state: ${user.family_name})",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

