package com.mahshad.network.models.forecast

import com.mahshad.network.models.ConditionDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DayDTO(
    @SerialName("maxtemp_c")
    val maxTempC: Double? = null,
    @SerialName("maxtemp_f")
    val maxTempF: Double? = null,
    @SerialName("mintemp_c")
    val minTempC: Double? = null,
    @SerialName("mintemp_f")
    val minTempF: Double? = null,
    @SerialName("avgtemp_c")
    val avgTempC: Double? = null,
    @SerialName("avgtemp_f")
    val avgTempF: Double? = null,
    @SerialName("maxwind_mph")
    val maxWindMph: Double? = null,
    @SerialName("maxwind_kph")
    val maxWindKph: Double? = null,
    @SerialName("totalprecip_mm")
    val totalPrecipitationMm: Double? = null,
    @SerialName("totalprecip_in")
    val totalPrecipitationIn: Double? = null,
    @SerialName("totalsnow_cm")
    val totalSnowCm: Double? = null,
    @SerialName("avgvis_km")
    val avgVisKm: Double? = null,
    @SerialName("avgvis_miles")
    val avgVisMiles: Double? = null,
    @SerialName("avghumidity")
    val avgHumidity: Int? = null,
    @SerialName("daily_will_it_rain")
    val dailyWillItRain: Int? = null,
    @SerialName("daily_chance_of_rain")
    val dailyChanceOfRain: Int? = null,
    @SerialName("daily_will_it_snow")
    val dailyWillItSnow: Int? = null,
    @SerialName("daily_chance_of_snow")
    val dailyChanceOfSnow: Int? = null,
    @SerialName("condition")
    val condition: ConditionDTO? = null,
    @SerialName("uv")
    val uv: Double? = null
) {
    companion object {
        val DEFAULT = DayDTO(
            maxTempC = 0.0,
            maxTempF = 0.0,
            minTempC = 0.0,
            minTempF = 0.0,
            avgTempC = 0.0,
            avgTempF = 0.0,
            maxWindMph = 0.0,
            maxWindKph = 0.0,
            totalPrecipitationMm = 0.0,
            totalPrecipitationIn = 0.0,
            totalSnowCm = 0.0,
            avgVisKm = 0.0,
            avgVisMiles = 0.0,
            avgHumidity = 0,
            dailyWillItRain = 0,
            dailyChanceOfRain = 0,
            dailyWillItSnow = 0,
            dailyChanceOfSnow = 0,
            condition = ConditionDTO.DEFAULT,
            uv = 0.0
        )
    }
}