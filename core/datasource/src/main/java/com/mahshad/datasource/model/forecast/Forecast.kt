package com.mahshad.datasource.model.forecast

import com.mahshad.datasource.model.currentweather.Current
import com.mahshad.datasource.model.currentweather.Location
import com.mahshad.datasource.model.currentweather.toCurrent
import com.mahshad.datasource.model.currentweather.toLocation
import com.mahshad.network.models.forecast.ForecastDTO

data class Forecast(
    val location: Location,
    val current: Current,
    val forecast: ForecastData
)

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