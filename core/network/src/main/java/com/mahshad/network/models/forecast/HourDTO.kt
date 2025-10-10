package com.mahshad.network.models.forecast

import com.mahshad.network.models.ConditionDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HourDTO(
    @SerialName("time_epoch")
    val timeEpoch: Long? = null,
    @SerialName("time")
    val time: String? = null,
    @SerialName("temp_c")
    val tempC: Double? = null,
    @SerialName("temp_f")
    val tempF: Double? = null,
    @SerialName("is_day")
    val isDay: Int? = null,
    @SerialName("condition")
    val condition: ConditionDTO? = null,
    @SerialName("wind_mph")
    val windMph: Double? = null,
    @SerialName("wind_kph")
    val windKph: Double? = null,
    @SerialName("wind_degree")
    val windDegree: Int? = null,
    @SerialName("wind_dir")
    val windDir: String? = null,
    @SerialName("pressure_mb")
    val pressureMb: Double? = null,
    @SerialName("pressure_in")
    val pressureIn: Double? = null,
    @SerialName("precip_mm")
    val precipitationMm: Double? = null,
    @SerialName("precip_in")
    val precipitationIn: Double? = null,
    @SerialName("snow_cm")
    val snowCm: Double? = null,
    @SerialName("snow_in")
    val snowIn: Double? = null,
    @SerialName("humidity")
    val humidity: Int? = null,
    @SerialName("cloud")
    val cloud: Int? = null,
    @SerialName("feelslike_c")
    val feelsLikeC: Double? = null,
    @SerialName("feelslike_f")
    val feelsLikeF: Double? = null,
    @SerialName("windchill_c")
    val windchillC: Double? = null,
    @SerialName("windchill_f")
    val windchillF: Double? = null,
    @SerialName("heatindex_c")
    val heatIndexC: Double? = null,
    @SerialName("heatindex_f")
    val heatIndexF: Double? = null,
    @SerialName("dewpoint_c")
    val dewPointC: Double? = null,
    @SerialName("dewpoint_f")
    val dewPointF: Double? = null,
    @SerialName("will_it_rain")
    val willItRain: Int? = null,
    @SerialName("chance_of_rain")
    val chanceOfRain: Int? = null,
    @SerialName("will_it_snow")
    val willItSnow: Int? = null,
    @SerialName("chance_of_snow")
    val chanceOfSnow: Int? = null,
    @SerialName("vis_km")
    val visKm: Double? = null,
    @SerialName("vis_miles")
    val visMiles: Double? = null,
    @SerialName("gust_mph")
    val gustMph: Double? = null,
    @SerialName("gust_kph")
    val gustKph: Double? = null,
    @SerialName("uv")
    val uv: Double? = null,
) {
    companion object {
        val DEFAULT = HourDTO(
            timeEpoch = 0L,
            time = "",
            tempC = 0.0,
            tempF = 0.0,
            isDay = 0,
            condition = ConditionDTO.DEFAULT,
            windMph = 0.0,
            windKph = 0.0,
            windDegree = 0,
            windDir = "N/A",
            pressureMb = 0.0,
            pressureIn = 0.0,
            precipitationMm = 0.0,
            precipitationIn = 0.0,
            snowCm = 0.0,
            snowIn = 0.0,
            humidity = 0,
            cloud = 0,
            feelsLikeC = 0.0,
            feelsLikeF = 0.0,
            windchillC = 0.0,
            windchillF = 0.0,
            heatIndexC = 0.0,
            heatIndexF = 0.0,
            dewPointC = 0.0,
            dewPointF = 0.0,
            willItRain = 0,
            chanceOfRain = 0,
            willItSnow = 0,
            chanceOfSnow = 0,
            visKm = 0.0,
            visMiles = 0.0,
            gustMph = 0.0,
            gustKph = 0.0,
            uv = 0.0
        )
    }
}