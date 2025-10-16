package com.mahshad.common.model.network.models.forecast

import com.mahshad.common.model.network.models.LocationDTO
import com.mahshad.common.model.network.models.currentweather.CurrentDTO
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
        val DEFAULT2 = ForecastDTO(
            null,
            null,
            null
        )
    }
}
