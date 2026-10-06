package com.example.domosense.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domosense.data.models.AirQuality
import com.example.domosense.ui.components.SensorCard
import com.example.domosense.ui.theme.DangerOrange
import com.example.domosense.ui.theme.DangerRed
import com.example.domosense.ui.theme.WarningYellow
import com.example.domosense.viewmodel.MainViewModel
import java.util.Locale

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    val data by viewModel.sensorData.collectAsState()

    val calidad = AirQuality.fromCO2(data.co2)
    val colorCO2 = when (calidad) {
        AirQuality.OPTIMO -> Color(0xFF00C853)
        AirQuality.REGULAR -> WarningYellow
        AirQuality.MALO -> DangerOrange
        AirQuality.CRITICO -> DangerRed
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "DOMO SENSE",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Monitor Ambiental",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        SensorCard(
            titulo = "TEMPERATURA",
            valor = String.format(Locale.US, "%.1f", data.temperatura),
            unidad = "°C",
            icono = "🌡️",
            colorAcento = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        SensorCard(
            titulo = "HUMEDAD",
            valor = String.format(Locale.US, "%.1f", data.humedad),
            unidad = "%",
            icono = "💧",
            colorAcento = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        SensorCard(
            titulo = "CO2",
            valor = data.co2.toString(),
            unidad = "ppm",
            icono = "🌫️",
            colorAcento = colorCO2,
            subtitulo = "Calidad: ${calidad.label}"
        )
    }
}