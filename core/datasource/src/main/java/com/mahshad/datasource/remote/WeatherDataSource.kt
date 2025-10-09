package com.mahshad.datasource.remote

import com.mahshad.datasource.model.currentweather.CurrentWeather
import com.mahshad.datasource.model.forecast.Forecast

interface WeatherDataSource {
    suspend fun getCurrentWeather(
        q: String,
        api: String
    ): Result<CurrentWeather>

    suspend fun getForecastWeather(q: String, api: String): Result<Forecast>
//    suspend fun searchLocation(cityName: String): Response<List<SearchDTO>>
}