package com.forecast.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.forecast.ui.component.TemperatureCurveChart
import com.mahshad.viewmodel.WeatherUIState
import com.mahshad.viewmodel.WeatherViewModel

@Composable
fun ForecastHomeScreen(
    navController: NavController,
    onNavigateToCurrentWeatherGraph: () -> Unit
) {
    val rootGraphId = remember(navController) { navController.graph.id }
    val backStackEntry = remember(navController.currentBackStackEntryAsState().value) {
        navController.getBackStackEntry(rootGraphId)
    }
    val viewModel: WeatherViewModel = hiltViewModel(
        viewModelStoreOwner = backStackEntry
    )
    val weatherUIState by viewModel.weatherUIState.collectAsStateWithLifecycle()

    Scaffold { innerPadding ->
        if (weatherUIState is WeatherUIState.Success) {
            (weatherUIState as WeatherUIState.Success).currentWeather.current
            val listOfHours =
                (weatherUIState as WeatherUIState.Success).forecast.forecast.forecastDay.flatMap {
                    it.hour
                }
            TemperatureCurveChart(listOfHours, modifier = Modifier.padding(innerPadding))
        }

    }
}