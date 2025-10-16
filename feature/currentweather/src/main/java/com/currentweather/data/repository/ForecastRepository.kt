package com.currentweather.data.repository

import com.mahshad.common.model.datasource.models.forecast.Forecast

interface ForecastRepository {
    suspend fun getForecast(
        location: String,
        dates: Int,
        aqi: Boolean,
        api: Boolean
    ): Result<Forecast>
}