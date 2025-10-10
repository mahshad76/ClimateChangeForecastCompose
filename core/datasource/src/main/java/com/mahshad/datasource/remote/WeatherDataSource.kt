package com.mahshad.datasource.remote

import com.mahshad.datasource.model.currentweather.CurrentWeather
import com.mahshad.datasource.model.forecast.Forecast
import com.mahshad.datasource.model.search.Search

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