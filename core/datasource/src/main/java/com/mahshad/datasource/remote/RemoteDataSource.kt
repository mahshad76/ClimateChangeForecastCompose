package com.mahshad.datasource.remote

import com.mahshad.datasource.model.currentweather.CurrentWeather

interface RemoteDataSource {
//    suspend fun getForecastWeather(q: String, api: String): Response<List<ForecastWeatherDTO>>

    suspend fun getCurrentWeather(
        q: String,
        days: Int,
        api: String,
        alert: String
    ): Result<CurrentWeather>

//    suspend fun searchLocation(cityName: String): Response<List<SearchDTO>>
}