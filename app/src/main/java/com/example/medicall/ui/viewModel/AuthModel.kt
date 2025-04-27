package com.example.medicall.ui.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.medicall.ui.entities.Users
import androidx.lifecycle.viewModelScope
import com.example.medicall.ui.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthModel(private val repository: AuthRepository): ViewModel() {
    val role =  mutableStateOf("")
    val loading = mutableStateOf(false)
    val error = mutableStateOf(false)
    val user = mutableStateOf<Users?>(null)


    fun authentication(loginMap: MutableState<Map<String, String>>) {
        loading.value = true
        viewModelScope.launch {
            try {
                role.value = repository.authentication(loginMap)
            }
            catch (e:Exception) {
                error.value = true
            }
            finally {
                loading.value = false
            }

        }
    }

    }
