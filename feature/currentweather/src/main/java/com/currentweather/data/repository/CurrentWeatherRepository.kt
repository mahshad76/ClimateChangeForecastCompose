package com.currentweather.data.repository

import com.mahshad.datasource.model.currentweather.CurrentWeather

interface CurrentWeatherRepository {
    suspend fun getCurrentWeather(
        q: String,
        days: Int,
        api: String,
        alert: String
    ): Result<CurrentWeather>
}