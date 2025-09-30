package com.currentweather.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.currentweather.ui.CurrentWeatherScreen


data object CurrentWeatherRoute

fun NavGraphBuilder.currentWeatherGraph() {
    navigation(
        startDestination = CurrentWeatherRoutes.CurrentWeatherHome,
        route = CurrentWeatherRoute::class,
        typeMap = TODO()
    ) {
        composable(CurrentWeatherRoutes.CurrentWeatherHome::class) {
            CurrentWeatherScreen()
        }
    }
}