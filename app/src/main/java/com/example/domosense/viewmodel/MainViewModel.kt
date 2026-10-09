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

data class Settings(
    val co2Max: Float = 1000f,
    val tempMin: Float = 18f,
    val tempMax: Float = 28f,
    val humMin: Float = 30f,
    val humMax: Float = 70f,
    val notifPush: Boolean = true,
    val sonidoAlerta: Boolean = true,
    val modoNoMolestar: Boolean = false,
    val usarFahrenheit: Boolean = false
)

class MainViewModel : ViewModel() {

    private val _sensorData = MutableStateFlow(SensorData())
    val sensorData: StateFlow<SensorData> = _sensorData.asStateFlow()

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
            repeat(30) {
                delay(100)
                addReading()
            }
            while (true) {
                delay(3000)
                addReading()
            }
        }
    }

    private fun addReading() {
        val newData = SensorData(
            temperatura = 18f + Random.nextFloat() * 10f,
            humedad = 40f + Random.nextFloat() * 30f,
            co2 = 400 + Random.nextInt(1100),
            timestamp = System.currentTimeMillis()
        )
        _sensorData.value = newData
        _history.value = (_history.value + newData).takeLast(50)
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

    fun toggleConnection() {
        _isConnected.value = !_isConnected.value
    }
}