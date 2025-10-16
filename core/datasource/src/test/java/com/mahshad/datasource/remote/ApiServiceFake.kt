package com.mahshad.datasource.remote


import com.mahshad.common.model.network.models.currentweather.CurrentWeatherDTO
import com.mahshad.common.model.network.models.forecast.ForecastDTO
import com.mahshad.common.model.network.models.search.SearchDTO
import com.mahshad.network.ApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

const val ERROR_STRING = "Bad Request"

class ApiServiceFake(
    override var enableError: Boolean = false,
    override var nullifyForbiddenAttributes: Boolean = false
) : ApiService {
    val mediaType = "text/plain; charset=utf-8".toMediaType()
    val errorResponseBody = ERROR_STRING.toResponseBody(mediaType)

    override suspend fun getCurrentWeather(q: String, api: String): Response<CurrentWeatherDTO> =
        if (enableError) Response<CurrentWeatherDTO>.error(400, errorResponseBody)
        else
            if (nullifyForbiddenAttributes) Response.success(CurrentWeatherDTO.DEFAULT2)
            else Response.success(CurrentWeatherDTO.DEFAULT)

    override suspend fun getForecastWeather(
        location: String,
        days: Int,
        aqi: Boolean,
        alerts: Boolean
    ): Response<ForecastDTO> =
        if (enableError) Response<ForecastDTO>.error(400, errorResponseBody)
        else
            if (nullifyForbiddenAttributes) Response.success(ForecastDTO.DEFAULT2)
            else Response.success(ForecastDTO.DEFAULT)

    override suspend fun searchLocation(cityName: String): Response<List<SearchDTO>> =
        if (enableError) Response<SearchDTO>.error(400, errorResponseBody)
        else Response.success(listOf(SearchDTO.DEFAULT))
}