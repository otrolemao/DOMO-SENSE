package com.example.domosense.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsSection(
    titulo: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = titulo,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))
    content()
}