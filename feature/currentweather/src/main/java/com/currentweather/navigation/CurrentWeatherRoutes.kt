package com.currentweather.navigation

import kotlinx.serialization.Serializable

sealed interface CurrentWeatherRoutes {
    @Serializable
    data object CurrentWeatherHome : CurrentWeatherRoutes
}