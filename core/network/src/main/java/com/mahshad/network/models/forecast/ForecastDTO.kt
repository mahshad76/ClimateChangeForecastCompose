package com.mahshad.network.models.forecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastDTO(
    @SerialName("location")
    val locationDTO: LocationDTO? = null,
    @SerialName("current")
    val currentDTO: CurrentDTO? = null,
    @SerialName("forecast")
    val forecastDTO: ForecastWeatherDTO? = null,
)