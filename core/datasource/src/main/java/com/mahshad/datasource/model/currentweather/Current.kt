package com.mahshad.datasource.model.currentweather

import com.mahshad.network.models.currentweather.CurrentDTO

data class Current(
    val cloud: Int,
    val condition: Condition,
    val feelsLikeC: Double,
    val feelsLikeF: Double,
    val humidity: Int,
    val isDay: Int,
    val lastUpdated: String,
    val precipIn: Double,
    val precipMm: Double,
    val pressureIn: Double,
    val pressureMb: Double,
    val tempC: Double,
    val tempF: Double,
    val uv: Double,
    val windDegree: Int,
    val windDir: String,
    val windKph: Double,
    val windMph: Double,
    val windchillC: Double,
    val windchillF: Double
) {
    companion object {
        val DEFAULT = Current(
            cloud = 0,
            condition = Condition.DEFAULT,
            feelsLikeC = 0.0,
            feelsLikeF = 0.0,
            humidity = 0,
            isDay = 0,
            lastUpdated = "",
            precipIn = 0.0,
            precipMm = 0.0,
            pressureIn = 0.0,
            pressureMb = 0.0,
            tempC = 0.0,
            tempF = 0.0,
            uv = 0.0,
            windDegree = 0,
            windDir = "",
            windKph = 0.0,
            windMph = 0.0,
            windchillC = 0.0,
            windchillF = 0.0
        )
    }
}

fun CurrentDTO.toCurrent(): Result<Current> {
    return runCatching {
        val requiredTempC = tempC
            ?: throw IllegalArgumentException("Temperature (C) is a mandatory field for Current and cannot be null.")
        val requiredTempF = tempF
            ?: throw IllegalArgumentException("Temperature (F) is a mandatory field for Current and cannot be null.")
        val requiredLastUpdated = lastUpdated
            ?: throw IllegalArgumentException("Last updated time is a mandatory field for Current and cannot be null.")
        val requiredConditionDTO = conditionDTO
            ?: throw IllegalArgumentException("Condition data is mandatory for Current and cannot be null.")

        Current(
            cloud = cloud ?: 0,
            condition = requiredConditionDTO.toCondition(),
            feelsLikeC = feelsLikeC ?: 0.0,
            feelsLikeF = feelsLikeF ?: 0.0,
            humidity = humidity ?: 0,
            isDay = isDay ?: 0,
            lastUpdated = requiredLastUpdated,
            precipIn = precipIn ?: 0.0,
            precipMm = precipMm ?: 0.0,
            pressureIn = pressureIn ?: 0.0,
            pressureMb = pressureMb ?: 0.0,
            tempC = requiredTempC,
            tempF = requiredTempF,
            uv = uv ?: 0.0,
            windDegree = windDegree ?: 0,
            windDir = windDir.orEmpty(),
            windKph = windKph ?: 0.0,
            windMph = windMph ?: 0.0,
            windchillC = windchillC ?: 0.0,
            windchillF = windchillF ?: 0.0
        )
    }
}
