package com.mahshad.datasource.remote

import com.mahshad.network.ApiService
import com.mahshad.network.models.currentweather.CurrentWeatherDTO
import com.mahshad.network.models.forecast.ForecastWeatherDTO
import com.mahshad.network.models.search.SearchDTO
import retrofit2.Response

class ApiServiceFake(enableError: Boolean) : ApiService {
    override suspend fun getForecastWeather(
        q: String,
        api: String
    ): Response<ForecastWeatherDTO> {

    }

    override suspend fun getCurrentWeather(
        q: String,
        days: Int,
        api: String,
        alert: String
    ): Response<CurrentWeatherDTO> {

    }

    override suspend fun searchLocation(cityName: String): Response<List<SearchDTO>> {

    }
}