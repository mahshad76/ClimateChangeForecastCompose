package com.currentweather.ui

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun CurrentWeatherHomeScreen(
    viewModel: CurrentWeatherHomeScreenViewModel = hiltViewModel<CurrentWeatherHomeScreenViewModel>(),
    onNavigateToForecastGraph: (String) -> Unit
) {
//    viewModel.getCurrentWeather(
//        "London",
//        api = "no"
//    )
//    viewModel.getForecast("London", 1, false, false)

    //onNavigateToForecastGraph.invoke("London")
    //Text("current weather screen")
}