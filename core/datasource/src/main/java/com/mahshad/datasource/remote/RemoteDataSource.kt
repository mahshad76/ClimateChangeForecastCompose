package com.mahshad.datasource.remote

import com.mahshad.network.models.currentweather.CurrentWeatherDTO
import retrofit2.Response

interface RemoteDataSource {
//    suspend fun getForecastWeather(q: String, api: String): Response<List<ForecastWeatherDTO>>

    suspend fun getCurrentWeather(q: String, days: Int, api: String, alert: String):
            Response<List<CurrentWeatherDTO>>

//    suspend fun searchLocation(cityName: String): Response<List<SearchDTO>>
}