package com.mahshad.network.models.currentweather

import com.mahshad.network.models.LocationDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherDTO(
    @SerialName("current") val currentDTO: CurrentDTO? = null,
    @SerialName("location") val locationDTO: LocationDTO? = null
) {
    companion object {
        val DEFAULT = CurrentWeatherDTO(
            CurrentDTO.DEFAULT,
            LocationDTO.DEFAULT
        )
    }
}