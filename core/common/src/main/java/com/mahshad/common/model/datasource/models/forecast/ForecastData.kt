package com.mahshad.common.model.datasource.models.forecast

import com.mahshad.common.model.network.models.forecast.ForecastWeatherDTO


data class ForecastData(
    val forecastDay: List<ForecastDay>
)

fun ForecastWeatherDTO.toForecastData(): ForecastData {
    return ForecastData(
        forecastDay = forecastDay.orEmpty()
            .mapNotNull { forecastDayDTO -> forecastDayDTO?.toForecastDayResult()?.getOrThrow() }
    )
}