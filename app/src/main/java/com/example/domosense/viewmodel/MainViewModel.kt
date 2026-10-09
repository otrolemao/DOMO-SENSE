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

    private val _history = MutableStateFlow<List<SensorData>>(emptyList())
    val history: StateFlow<List<SensorData>> = _history.asStateFlow()

    private val _isConnected = MutableStateFlow(true)
    val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    init {
        startSimulation()
    }

    private fun startSimulation() {
        viewModelScope.launch {
            // Historial inicial de 30 muestras para que se vea el gráfico
            repeat(30) {
                delay(100)
                addReading()
            }
            // Luego, cada 3 segundos, agregamos una nueva lectura
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
        // Mantener solo las últimas 50 lecturas
        _history.value = (_history.value + newData).takeLast(50)
    }

    fun toggleConnection() {
        _isConnected.value = !_isConnected.value
    }
}