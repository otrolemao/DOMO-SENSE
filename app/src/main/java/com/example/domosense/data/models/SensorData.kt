package com.example.domosense.data.models

data class SensorData(
    val temperatura: Float = 0f,
    val humedad: Float = 0f,
    val co2: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)

// Nivel de calidad del aire según CO2
enum class AirQuality(val label: String) {
    OPTIMO("Óptimo"),
    REGULAR("Regular"),
    MALO("Malo"),
    CRITICO("Crítico");

    companion object {
        fun fromCO2(ppm: Int): AirQuality = when {
            ppm < 600 -> OPTIMO
            ppm < 1000 -> REGULAR
            ppm < 1500 -> MALO
            else -> CRITICO
        }
    }
}