package com.currentweather.ui

import androidx.compose.runtime.Composable

@Composable
fun CurrentWeatherHomeScreen(onNavigateToForecastGraph: (String) -> Unit) {
    onNavigateToForecastGraph.invoke("London")
    //Text("current weather screen")
}