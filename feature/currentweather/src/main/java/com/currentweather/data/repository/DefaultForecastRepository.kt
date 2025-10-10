package com.currentweather.data.repository

import com.mahshad.datasource.remote.WeatherDataSource
import javax.inject.Inject

class DefaultForecastRepository @Inject constructor(
    private val weatherDataSource: WeatherDataSource
) :
    ForecastRepository {
    override suspend fun getForecast(location: String, dates: Int, aqi: Boolean, api: Boolean) =
        weatherDataSource.getForecastWeather(location, dates, aqi, api)
}