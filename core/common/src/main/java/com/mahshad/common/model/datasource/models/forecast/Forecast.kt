package com.mahshad.common.model.datasource.models.forecast

import com.mahshad.common.model.datasource.models.currentweather.Current
import com.mahshad.common.model.datasource.models.currentweather.Location
import com.mahshad.common.model.datasource.models.currentweather.toCurrent
import com.mahshad.common.model.datasource.models.currentweather.toLocation
import com.mahshad.common.model.network.models.forecast.ForecastDTO


data class Forecast(
    val location: Location,
    val current: Current,
    val forecast: ForecastData
) {
    companion object {
        val DEFAULT = ForecastDTO.DEFAULT.toForecastResult()
    }
}

fun ForecastDTO.toForecastResult(): Result<Forecast> {
    return runCatching {
        val currentDtoNonNull = currentDTO
            ?: throw IllegalArgumentException("CurrentDTO is missing for Forecast and cannot be null.")
        val locationDtoNonNull = locationDTO
            ?: throw IllegalArgumentException("LocationDTO is missing for Forecast and cannot be null.")
        val forecastDtoNonNull = forecastDTO
            ?: throw IllegalArgumentException("ForecastDTO is missing for Forecast and cannot be null.")

        val current = currentDtoNonNull.toCurrent().getOrThrow()
        val location = locationDtoNonNull.toLocation().getOrThrow()
        val forecast = forecastDtoNonNull.toForecastData()

        Forecast(
            current = current,
            location = location,
            forecast = forecast
        )
    }
}