package com.mahshad.network.models.forecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastWeatherDTO(
    @SerialName("forecastday")
    val forecastDay: List<ForecastDayDTO?>? = null,
) {
    companion object {
        val DEFAULT = ForecastWeatherDTO(
            listOf(ForecastDayDTO.DEFAULT)
        )
    }
}
