package com.forecast.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mahshad.common.model.datasource.models.currentweather.Condition
import com.mahshad.common.model.datasource.models.currentweather.Current


@Composable
fun WeatherDetailCard(current: Current) {
    val isDay = current.isDay == 1
    val cardColor = if (isDay) Color(0xFFE3F2FD) else Color(0xFF1E88E5)
    val contentColor = if (isDay) Color.Black else Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Placeholder for Weather Icon (In a real app, this would load the condition.icon)
//                Icon(
//                    imageVector = Icons.Filled.WbSunny,
//                    contentDescription = "Weather Icon",
//                    modifier = Modifier.size(64.dp),
//                    tint = contentColor
//                )
                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "${current.tempC}°C",
                        fontSize = 52.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 56.sp // Prevent text from being clipped
                    )
                    Text(
                        text = current.condition.text,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Secondary Metrics Grid (2x2)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // First Row of details
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
//                    WeatherDetailItem(
//                        icon = Icons.Filled.Thermostat,
//                        label = "Feels Like",
//                        value = "${current.feelsLikeC}°C"
//                    )
//                    WeatherDetailItem(
//                        icon = Icons.Filled.Air,
//                        label = "Wind",
//                        value = "${current.windKph} kph"
//                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Second Row of details
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
//                    WeatherDetailItem(
//                        icon = Icons.Filled.WaterDrop,
//                        label = "Humidity",
//                        value = "${current.humidity}%"
//                    )
//                    WeatherDetailItem(
//                        icon = Icons.Filled.WbSunny, // Reusing icon for UV, better to use a dedicated icon if available
//                        label = "UV Index",
//                        value = "${current.uv}"
//                    )
                }
            }
        }
    }
}

// Helper composable for the detail items in the grid
@Composable
fun RowScope.WeatherDetailItem(icon: ImageVector, label: String, value: String) {
    Column(
        modifier = Modifier
            .weight(1f) // Ensures equal spacing across the row
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = LocalContentColor.current.copy(alpha = 0.7f)
        )
    }
}

// Example Preview Composable (for demonstration purposes only)

@Preview
@Composable
fun PreviewWeatherDetailCard() {
    val sampleCondition = Condition.DEFAULT
    val sampleCurrent = Current(
        cloud = 0,
        condition = sampleCondition,
        feelsLikeC = 28.5,
        feelsLikeF = 83.3,
        humidity = 60,
        isDay = 1, // Day time
        lastUpdated = "2024-01-01 12:00",
        precipIn = 0.0,
        precipMm = 0.0,
        pressureIn = 30.0,
        pressureMb = 1016.0,
        tempC = 30.2,
        tempF = 86.4,
        uv = 7.0,
        windDegree = 270,
        windDir = "W",
        windKph = 15.5,
        windMph = 9.6,
        windchillC = 30.2,
        windchillF = 86.4
    )
    MaterialTheme {
        WeatherDetailCard(current = sampleCurrent)
    }
}