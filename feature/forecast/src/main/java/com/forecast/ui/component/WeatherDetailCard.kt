package com.forecast.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
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
                AsyncImage(
                    model = current.condition.icon,
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "${current.tempC}°C",
                        fontSize = 52.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 56.sp
                    )
                    Text(
                        text = current.condition.text,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    WeatherDetailItem(
                        icon = Icons.Filled.Thermostat,
                        label = "Feels Like",
                        value = "${current.feelsLikeC}°C"
                    )
                    WeatherDetailItem(
                        icon = Icons.Filled.Air,
                        label = "Wind",
                        value = "${current.windKph} kph"
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    WeatherDetailItem(
                        icon = Icons.Filled.WaterDrop,
                        label = "Humidity",
                        value = "${current.humidity}%"
                    )
                    WeatherDetailItem(
                        icon = Icons.Filled.WbSunny,
                        label = "UV Index",
                        value = "${current.uv}"
                    )
                }
            }
        }
    }
}

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