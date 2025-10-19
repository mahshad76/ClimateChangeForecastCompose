package com.forecast.navigation

import kotlinx.serialization.Serializable

sealed interface ForecastRoutes {
    @Serializable
    object ForecastHome
}