package com.example.domosense.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domosense.data.models.SensorData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

// ===== Estados de UI =====
sealed class UiState {
    object Loading : UiState()
    data class Success(val data: SensorData) : UiState()
    data class Error(val mensaje: String) : UiState()
    object Empty : UiState()
}

// ===== Modo de simulación =====
enum class SimulationMode(val label: String) {
    AUTOMATICO("Automático"),
    FORZAR_LOADING("Forzar Loading"),
    FORZAR_ERROR("Forzar Error"),
    FORZAR_EMPTY("Forzar Empty")
}

data class Settings(
    val co2Max: Float = 1000f,
    val tempMin: Float = 18f,
    val tempMax: Float = 28f,
    val humMin: Float = 30f,
    val humMax: Float = 70f,
    val notifPush: Boolean = true,
    val sonidoAlerta: Boolean = true,
    val modoNoMolestar: Boolean = false,
    val usarFahrenheit: Boolean = false,
    val simulationMode: SimulationMode = SimulationMode.AUTOMATICO
)

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _history = MutableStateFlow<List<SensorData>>(emptyList())
    val history: StateFlow<List<SensorData>> = _history.asStateFlow()

    private val _isConnected = MutableStateFlow(true)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val _settings = MutableStateFlow(Settings())
    val settings: StateFlow<Settings> = _settings.asStateFlow()

    init {
        startSimulation()
    }

    private fun startSimulation() {
        viewModelScope.launch {
            // Primero mostramos Loading durante 2 segundos
            _uiState.value = UiState.Loading
            delay(2000)

            // Historial inicial (silencioso, no toca el uiState)
            repeat(30) {
                delay(50)
                addReading(silent = true)
            }

            // Luego pasamos a Success
            addReading()

            // Loop infinito: cada 3 segundos intentamos agregar una lectura
            // (pero addReading respeta el modo de simulación)
            while (true) {
                delay(3000)
                addReading()
            }
        }
    }

    private fun addReading(silent: Boolean = false) {
        // Verificar el modo de simulación PRIMERO
        val modo = _settings.value.simulationMode

        when (modo) {
            SimulationMode.FORZAR_LOADING -> {
                _uiState.value = UiState.Loading
                return  // ← No actualizar nada más
            }
            SimulationMode.FORZAR_ERROR -> {
                _uiState.value = UiState.Error("No se pudo conectar con DOMO SENSE-01")
                return  // ← No actualizar nada más
            }
            SimulationMode.FORZAR_EMPTY -> {
                _uiState.value = UiState.Empty
                return  // ← No actualizar nada más
            }
            SimulationMode.AUTOMATICO -> {
                // Continuar con el flujo normal
            }
        }

        // Flujo normal (solo en modo AUTOMATICO)
        val newData = SensorData(
            temperatura = 18f + Random.nextFloat() * 10f,
            humedad = 40f + Random.nextFloat() * 30f,
            co2 = 400 + Random.nextInt(1100),
            timestamp = System.currentTimeMillis()
        )

        if (!silent) {
            _uiState.value = UiState.Success(newData)
            _history.value = (_history.value + newData).takeLast(50)
        } else {
            _history.value = (_history.value + newData).takeLast(50)
        }
    }

    // Reintentar después de un error
    fun retry() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            delay(1500)
            // Volver a automático
            _settings.value = _settings.value.copy(
                simulationMode = SimulationMode.AUTOMATICO
            )
            addReading()
        }
    }

    // ========== Actualizadores de Settings ==========

    fun updateCo2Max(value: Float) {
        _settings.value = _settings.value.copy(co2Max = value)
    }

    fun updateTempMin(value: Float) {
        _settings.value = _settings.value.copy(tempMin = value)
    }

    fun updateTempMax(value: Float) {
        _settings.value = _settings.value.copy(tempMax = value)
    }

    fun updateHumMin(value: Float) {
        _settings.value = _settings.value.copy(humMin = value)
    }

    fun updateHumMax(value: Float) {
        _settings.value = _settings.value.copy(humMax = value)
    }

    fun updateNotifPush(value: Boolean) {
        _settings.value = _settings.value.copy(notifPush = value)
    }

    fun updateSonidoAlerta(value: Boolean) {
        _settings.value = _settings.value.copy(sonidoAlerta = value)
    }

    fun updateModoNoMolestar(value: Boolean) {
        _settings.value = _settings.value.copy(modoNoMolestar = value)
    }

    fun updateUsarFahrenheit(value: Boolean) {
        _settings.value = _settings.value.copy(usarFahrenheit = value)
    }

    fun updateSimulationMode(mode: SimulationMode) {
        _settings.value = _settings.value.copy(simulationMode = mode)
        // Al cambiar el modo, aplicarlo inmediatamente
        when (mode) {
            SimulationMode.FORZAR_LOADING -> _uiState.value = UiState.Loading
            SimulationMode.FORZAR_ERROR -> _uiState.value = UiState.Error("No se pudo conectar con DOMO SENSE-01")
            SimulationMode.FORZAR_EMPTY -> _uiState.value = UiState.Empty
            SimulationMode.AUTOMATICO -> {
                _uiState.value = UiState.Loading
                viewModelScope.launch {
                    delay(1000)
                    addReading()
                }
            }
        }
    }

    fun toggleConnection() {
        _isConnected.value = !_isConnected.value
    }
}