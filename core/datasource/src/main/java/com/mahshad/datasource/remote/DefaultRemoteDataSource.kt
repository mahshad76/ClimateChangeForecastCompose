package com.mahshad.datasource.remote

import com.mahshad.datasource.model.currentweather.CurrentWeather
import com.mahshad.network.ApiService
import com.mahshad.network.models.currentweather.CurrentWeatherDTO
import com.mahshad.network.models.forecast.ForecastWeatherDTO
import com.mahshad.network.models.search.SearchDTO
import retrofit2.Response

class DefaultRemoteDataSource(private val apiService: ApiService) : RemoteDataSource {
//    override suspend fun getForecastWeather(
//        q: String,
//        api: String
//    ): List<ForecastWeather>


    override suspend fun getCurrentWeather(
        q: String,
        days: Int,
        api: String,
        alert: String
    ): List<CurrentWeather>{

    }

//    override suspend fun searchLocation(cityName: String): List<Search>
}