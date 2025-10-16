package com.mahshad.datasource.remote

import com.mahshad.common.model.datasource.models.currentweather.CurrentWeather
import com.mahshad.common.model.datasource.models.forecast.Forecast
import com.mahshad.common.model.datasource.models.search.Search

interface WeatherDataSource {
    suspend fun getCurrentWeather(
        q: String,
        api: String
    ): Result<CurrentWeather>

    suspend fun getForecastWeather(
        location: String,
        days: Int,
        aqi: Boolean,
        alerts: Boolean
    ): Result<Forecast>

    suspend fun searchLocation(cityName: String): Result<List<Search>>
}