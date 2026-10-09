package com.example.domosense.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domosense.ui.charts.LineChart
import com.example.domosense.ui.theme.DangerOrange
import com.example.domosense.ui.theme.DangerRed
import com.example.domosense.ui.theme.WarningYellow
import com.example.domosense.viewmodel.MainViewModel
import java.util.Locale

enum class TimeRange(val label: String) {
    HOY("Hoy"),
    SEMANA("Semana"),
    MES("Mes")
}

@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    val history by viewModel.history.collectAsState()
    val context = LocalContext.current
    var selectedRange by remember { mutableStateOf(TimeRange.HOY) }

    // Extraer las listas de valores para el gráfico
    val temperaturas = history.map { it.temperatura }
    val humedades = history.map { it.humedad }
    val co2List = history.map { it.co2.toFloat() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Título
        Text(
            text = "HISTORIAL",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "${history.size} muestras registradas",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tabs de rango de tiempo
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TimeRange.entries.forEach { range ->
                FilterChip(
                    selected = selectedRange == range,
                    onClick = { selectedRange = range },
                    label = { Text(range.label) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = Color.Black
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Gráfico de temperatura
        LineChart(
            data = temperaturas,
            lineColor = MaterialTheme.colorScheme.primary,
            label = "TEMPERATURA",
            unit = "°C"
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Gráfico de humedad
        LineChart(
            data = humedades,
            lineColor = MaterialTheme.colorScheme.secondary,
            label = "HUMEDAD",
            unit = "%"
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Gráfico de CO2
        LineChart(
            data = co2List,
            lineColor = WarningYellow,
            label = "CO2",
            unit = "ppm"
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Sección de estadísticas
        Text(
            text = "ESTADÍSTICAS",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Fila 1: Temperatura
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                titulo = "TEMP. PROM.",
                valor = if (temperaturas.isNotEmpty())
                    String.format(Locale.US, "%.1f °C", temperaturas.average())
                else "--",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                titulo = "TEMP. MÁX.",
                valor = if (temperaturas.isNotEmpty())
                    String.format(Locale.US, "%.1f °C", temperaturas.max())
                else "--",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Fila 2: Humedad
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                titulo = "HUM. PROM.",
                valor = if (humedades.isNotEmpty())
                    String.format(Locale.US, "%.1f %%", humedades.average())
                else "--",
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                titulo = "HUM. MÁX.",
                valor = if (humedades.isNotEmpty())
                    String.format(Locale.US, "%.1f %%", humedades.max())
                else "--",
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Fila 3: CO2
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                titulo = "CO2 PROM.",
                valor = if (co2List.isNotEmpty())
                    String.format(Locale.US, "%.0f ppm", co2List.average())
                else "--",
                color = WarningYellow,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                titulo = "CO2 MÁX.",
                valor = if (co2List.isNotEmpty())
                    String.format(Locale.US, "%.0f ppm", co2List.max())
                else "--",
                color = WarningYellow,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de exportar
        Button(
            onClick = {
                Toast.makeText(
                    context,
                    "Función de exportación próximamente",
                    Toast.LENGTH_SHORT
                ).show()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.Black
            )
        ) {
            Text(
                text = "📥  Exportar datos",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun StatCard(
    titulo: String,
    valor: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = titulo,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = valor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}