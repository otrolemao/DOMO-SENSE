package com.example.domosense.ui.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LineChart(
    data: List<Float>,
    lineColor: Color,
    label: String,
    unit: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header con label y valor actual
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(lineColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = label,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                if (data.isNotEmpty()) {
                    Text(
                        text = "${String.format("%.1f", data.last())} $unit",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = lineColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Gráfico
            if (data.size < 2) {
                // Empty state
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Sin datos suficientes",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            } else {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                ) {
                    val maxValue = data.max()
                    val minValue = data.min()
                    val range = if (maxValue - minValue == 0f) 1f else maxValue - minValue

                    val width = size.width
                    val height = size.height
                    val stepX = width / (data.size - 1)

                    val path = Path()

                    data.forEachIndexed { index, value ->
                        val normalizedValue = (value - minValue) / range
                        val x = index * stepX
                        val y = height - (normalizedValue * height)

                        if (index == 0) {
                            path.moveTo(x, y)
                        } else {
                            path.lineTo(x, y)
                        }
                    }

                    drawPath(
                        path = path,
                        color = lineColor,
                        style = Stroke(width = 3f)
                    )

                    // Punto del último valor
                    val lastX = width
                    val lastNormalized = (data.last() - minValue) / range
                    val lastY = height - (lastNormalized * height)
                    drawCircle(
                        color = lineColor,
                        radius = 5f,
                        center = Offset(lastX, lastY)
                    )
                }
            }
        }
    }
}