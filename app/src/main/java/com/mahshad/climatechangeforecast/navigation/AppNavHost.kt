package com.mahshad.climatechangeforecast.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.currentweather.navigation.CurrentWeatherRoute
import com.currentweather.navigation.CurrentWeatherRoutes
import com.currentweather.navigation.currentWeatherGraph

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = CurrentWeatherRoute,
        modifier = modifier
    ) {
        currentWeatherGraph(navController = navController)
    }
}

fun NavController.navigateToCurrentWeatherGraph() {
    navigate(CurrentWeatherRoutes.CurrentWeatherHome) {
        popUpTo(graph.startDestinationId) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

fun NavController.navigateToForecastGraph() {
    navigate(CurrentWeatherRoutes.CurrentWeatherHome) {
        popUpTo(graph.startDestinationId) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }

}