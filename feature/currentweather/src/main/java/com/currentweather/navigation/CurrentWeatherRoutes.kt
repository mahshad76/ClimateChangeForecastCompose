package com.currentweather.navigation

sealed interface CurrentWeatherRoutes {
    data object CurrentWeatherHome : CurrentWeatherRoutes
}