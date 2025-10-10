package com.currentweather.data.repository

import com.mahshad.datasource.model.forecast.Forecast

interface ForecastRepository {
    suspend fun getForecast(
        location: String,
        dates: Int,
        aqi: Boolean,
        api: Boolean
    ): Result<Forecast>
}