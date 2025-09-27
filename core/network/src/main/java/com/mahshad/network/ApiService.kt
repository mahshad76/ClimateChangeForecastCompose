package com.mahshad.network

import com.mahshad.network.models.currentweather.CurrentWeatherDTO
import com.mahshad.network.models.forecast.ForecastWeatherDTO
import com.mahshad.network.models.search.SearchDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    //    one possible query
    @GET("forecast.json")
    suspend fun getForecastWeather(
        @Query("q") q: String,
        @Query("api") api: String = "no"
    ):
            Response<List<ForecastWeatherDTO>>

    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("q") q: String,
        @Query("days") days: Int = 1,
        @Query("api") api: String = "no",
        @Query("alert") alert: String = "no"
    ):
            Response<List<CurrentWeatherDTO>>

    @GET("search.json")
    fun searchLocation(
        @Query("q") cityName: String
    ):
            Response<List<SearchDTO>>
}