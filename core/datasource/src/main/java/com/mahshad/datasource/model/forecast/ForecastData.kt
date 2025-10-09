package com.mahshad.datasource.model.forecast

import com.currentweather.data.model.forecast.ForecastDay
import com.currentweather.data.model.forecast.toForecastDayResult
import com.mahshad.network.models.forecast.ForecastWeatherDTO


data class ForecastData(
    val forecastDay: List<ForecastDay>
)

fun ForecastWeatherDTO.toForecastData(): ForecastData {
    return ForecastData(
        forecastDay = forecastDay.orEmpty()
            .mapNotNull { forecastDayDTO -> forecastDayDTO?.toForecastDayResult()?.getOrThrow() }
    )
}