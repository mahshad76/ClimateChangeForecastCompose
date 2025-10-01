package com.currentweather.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.currentweather.ui.CurrentWeatherScreen
import kotlinx.serialization.Serializable

@Serializable
data object CurrentWeatherRoute

fun NavGraphBuilder.currentWeatherGraph(
    navController: NavController
) {
    navigation(
        startDestination = CurrentWeatherRoutes.CurrentWeatherHome,
        route = CurrentWeatherRoute::class
    ) {
        composable(CurrentWeatherRoutes.CurrentWeatherHome::class) {
            CurrentWeatherScreen()
        }
    }
}