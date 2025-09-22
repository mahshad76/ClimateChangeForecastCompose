package com.mahshad.network.models.forecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastDataDTO(
    @SerialName("forecastday")
    val forecastDay: List<ForecastDayDTO?>? = null,
)
