package com.mahshad.common.model.network.models.currentweather

import com.mahshad.common.model.network.models.LocationDTO
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
        val DEFAULT2 = DEFAULT.copy(
            currentDTO = null
        )
    }
}