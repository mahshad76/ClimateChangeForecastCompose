package com.forecast.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mahshad.viewmodel.WeatherViewModel

@Composable
fun ForecastHomeScreen(
    navController: NavController,
    location: String
) {
    val rootGraphId = remember(navController) { navController.graph.id }
    val backStackEntry = remember(navController.currentBackStackEntryAsState().value) {
        navController.getBackStackEntry(rootGraphId)
    }
    val viewModel: WeatherViewModel = hiltViewModel(
        viewModelStoreOwner = backStackEntry
    )

    Text("Forecast home screen ${location}")
}