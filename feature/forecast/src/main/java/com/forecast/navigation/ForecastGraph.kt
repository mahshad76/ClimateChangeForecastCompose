package com.forecast.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.forecast.ui.ForecastHomeScreen
import kotlinx.serialization.Serializable


@Serializable
data object ForecastRoute

fun NavGraphBuilder.forecastGraph(
    navController: NavController,
    onNavigateToCurrentWeatherGraph: () -> Unit
) {
    navigation(
        startDestination = ForecastRoutes.ForecastHome::class,
        route = ForecastRoute::class
    ) {
        composable(ForecastRoutes.ForecastHome::class) {
            ForecastHomeScreen(
                navController = navController,
                onNavigateToCurrentWeatherGraph = onNavigateToCurrentWeatherGraph
            )
        }
    }
}