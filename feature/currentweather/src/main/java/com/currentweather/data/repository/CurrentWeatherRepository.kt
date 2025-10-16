package com.currentweather.data.repository

import com.mahshad.common.model.datasource.models.currentweather.CurrentWeather

interface CurrentWeatherRepository {
    suspend fun getCurrentWeather(
        q: String,
        api: String
    ): Result<CurrentWeather>
}