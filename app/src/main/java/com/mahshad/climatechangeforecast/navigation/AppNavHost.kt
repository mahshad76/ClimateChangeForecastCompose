package com.mahshad.climatechangeforecast.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.currentweather.navigation.currentWeatherGraph

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    NavHost(
        navController = rememberNavController(),
        startDestination = "",
        modifier = modifier
    ) {
        currentWeatherGraph()
    }
}