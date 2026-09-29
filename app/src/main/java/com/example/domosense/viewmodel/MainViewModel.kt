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

class MainViewModel : ViewModel() {

    private val _sensorData = MutableStateFlow(SensorData())
    val sensorData: StateFlow<SensorData> = _sensorData.asStateFlow()

    private val _isConnected = MutableStateFlow(true)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    init {
        startSimulation()
    }

    private fun startSimulation() {
        viewModelScope.launch {
            while (true) {
                delay(3000) // Cada 3 segundos
                _sensorData.value = SensorData(
                    temperatura = 18f + Random.nextFloat() * 10f,   // 18-28 °C
                    humedad = 40f + Random.nextFloat() * 30f,       // 40-70 %
                    co2 = 400 + Random.nextInt(1100),               // 400-1500 ppm
                    timestamp = System.currentTimeMillis()
                )
            }
        }
    }

    fun toggleConnection() {
        _isConnected.value = !_isConnected.value
    }
}