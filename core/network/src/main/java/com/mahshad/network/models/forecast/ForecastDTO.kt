package com.mahshad.network.models.forecast

import com.mahshad.network.models.LocationDTO
import com.mahshad.network.models.currentweather.CurrentDTO
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
) {
    companion object {
        val DEFAULT = ForecastDTO(
            LocationDTO.DEFAULT,
            CurrentDTO.DEFAULT,
            ForecastWeatherDTO.DEFAULT
        )
    }
}