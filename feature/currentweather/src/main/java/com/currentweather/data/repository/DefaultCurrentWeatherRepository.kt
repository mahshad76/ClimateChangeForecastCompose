package com.currentweather.data.repository

import com.mahshad.datasource.model.currentweather.CurrentWeather
import com.mahshad.datasource.remote.WeatherDataSource
import javax.inject.Inject

class DefaultCurrentWeatherRepository @Inject constructor(
    private val weatherDataSource: WeatherDataSource
) : CurrentWeatherRepository {
    override suspend fun getCurrentWeather(
        q: String,
        api: String
    ): Result<CurrentWeather> {
        return weatherDataSource.getCurrentWeather(q, api)
    }
}