package com.example.api_kotlin

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.IOException

data class InterpolUiState(
    val notices: List<Notice> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class InterpolViewModel : ViewModel() {

    var uiState by mutableStateOf(InterpolUiState())
        private set

    private val apiService = ApiService.create()

    init {
        fetchNotices()
    }

    private fun fetchNotices() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            try {
                Log.d("InterpolViewModel", "Iniciando llamada a la API...")
                // 1. Llamamos a la nueva función getUsers()
                val users = apiService.getUsers()
                // 2. Convertimos la lista de UserResponse a una lista de Notice
                val notices = users.map { Notice(it) }

                Log.d("InterpolViewModel", "Respuesta recibida: ${notices.size} resultados")

                uiState = uiState.copy(
                    notices = notices, // Actualizamos la UI con la lista convertida
                    isLoading = false
                )
            } catch (e: retrofit2.HttpException) {
                Log.e("InterpolViewModel", "Error HTTP ${e.code()}: ${e.message()}")
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Error HTTP ${e.code()}: ${e.message()}"
                )
            } catch (e: IOException) {
                Log.e("InterpolViewModel", "Error de red: ${e.message}")
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Error de red: ${e.message}"
                )
            } catch (e: Exception) {
                Log.e("InterpolViewModel", "Error inesperado: ${e.message}", e)
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Error: ${e.message}"
                )
            }
        }
    }
}
