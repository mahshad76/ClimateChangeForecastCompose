package com.mahshad.network.models.forecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AstroDTO(
    @SerialName("sunrise")
    val sunrise: String? = null,
    @SerialName("sunset")
    val sunset: String? = null,
    @SerialName("moonrise")
    val moonrise: String? = null,
    @SerialName("moonset")
    val moonset: String? = null,
    @SerialName("moon_phase")
    val moonPhase: String? = null,
    @SerialName("moon_illumination")
    val moonIllumination: Int? = null,
    @SerialName("is_moon_up")
    val isMoonUp: Int? = null,
    @SerialName("is_sun_up")
    val isSunUp: Int? = null,
) {
    companion object {
        val DEFAULT = AstroDTO(
            sunrise = "06:30 AM",
            sunset = "07:45 PM",
            moonrise = "11:00 PM",
            moonset = "09:00 AM",
            moonPhase = "Waning Gibbous",
            moonIllumination = 85,
            isMoonUp = 1,
            isSunUp = 1
        )
    }
}