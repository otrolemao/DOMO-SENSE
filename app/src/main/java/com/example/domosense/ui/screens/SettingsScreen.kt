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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domosense.ui.components.SettingsSection
import com.example.domosense.ui.components.SliderSetting
import com.example.domosense.ui.components.SwitchSetting
import com.example.domosense.viewmodel.MainViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    val settings by viewModel.settings.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Título
        Text(
            text = "AJUSTES",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Personaliza tu experiencia",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        // ===== UMBRALES DE ALERTA =====
        SettingsSection(titulo = "🚨 UMBRALES DE ALERTA") {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SliderSetting(
                        titulo = "CO2 máximo",
                        valor = settings.co2Max,
                        onValueChange = { viewModel.updateCo2Max(it) },
                        valueRange = 600f..5000f,
                        unidad = "ppm"
                    )
                    SliderSetting(
                        titulo = "Temperatura mínima",
                        valor = settings.tempMin,
                        onValueChange = { viewModel.updateTempMin(it) },
                        valueRange = 5f..25f,
                        unidad = "°C"
                    )
                    SliderSetting(
                        titulo = "Temperatura máxima",
                        valor = settings.tempMax,
                        onValueChange = { viewModel.updateTempMax(it) },
                        valueRange = 20f..40f,
                        unidad = "°C"
                    )
                    SliderSetting(
                        titulo = "Humedad mínima",
                        valor = settings.humMin,
                        onValueChange = { viewModel.updateHumMin(it) },
                        valueRange = 10f..50f,
                        unidad = "%"
                    )
                    SliderSetting(
                        titulo = "Humedad máxima",
                        valor = settings.humMax,
                        onValueChange = { viewModel.updateHumMax(it) },
                        valueRange = 50f..90f,
                        unidad = "%"
                    )
                }
            }
        }

        // ===== NOTIFICACIONES =====
        SettingsSection(titulo = "🔔 NOTIFICACIONES") {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SwitchSetting(
                        titulo = "Notificaciones push",
                        checked = settings.notifPush,
                        onCheckedChange = { viewModel.updateNotifPush(it) }
                    )
                    SwitchSetting(
                        titulo = "Sonido de alerta",
                        checked = settings.sonidoAlerta,
                        onCheckedChange = { viewModel.updateSonidoAlerta(it) }
                    )
                    SwitchSetting(
                        titulo = "Modo no molestar",
                        checked = settings.modoNoMolestar,
                        onCheckedChange = { viewModel.updateModoNoMolestar(it) }
                    )
                }
            }
        }

        // ===== PERSONALIZACIÓN =====
        SettingsSection(titulo = "🎨 PERSONALIZACIÓN") {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Unidades de temperatura",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = !settings.usarFahrenheit,
                            onClick = { viewModel.updateUsarFahrenheit(false) },
                            label = { Text("Celsius °C") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = Color.Black
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = settings.usarFahrenheit,
                            onClick = { viewModel.updateUsarFahrenheit(true) },
                            label = { Text("Fahrenheit °F") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = Color.Black
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // ===== INFORMACIÓN =====
        SettingsSection(titulo = "ℹ️ INFORMACIÓN") {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoRow(titulo = "Versión", valor = "1.0.0")
                    InfoRow(titulo = "Dispositivo", valor = "DOMO SENSE-01")
                    InfoRow(
                        titulo = "Estado",
                        valor = "🟢 Conectado"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun InfoRow(titulo: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = titulo,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
        Text(
            text = valor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}