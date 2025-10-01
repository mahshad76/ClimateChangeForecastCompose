package com.forecast.navigation

import kotlinx.serialization.Serializable

sealed interface ForecastRoutes {
    @Serializable
    data class ForecastHome(val location: String)
}