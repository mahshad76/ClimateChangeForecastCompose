package com.mahshad.datasource.remote


import com.mahshad.network.ApiService
import com.mahshad.network.models.currentweather.CurrentWeatherDTO
import com.mahshad.network.models.forecast.ForecastDTO
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
    val errorResponse: Response<CurrentWeatherDTO> = Response.error(
        400,
        errorResponseBody
    )

    override suspend fun getCurrentWeather(q: String, api: String): Response<CurrentWeatherDTO> =
        if (enableError) errorResponse
        else
            if (nullifyForbiddenAttributes) Response.success(CurrentWeatherDTO.DEFAULT2)
            else Response.success(CurrentWeatherDTO.DEFAULT)

    override suspend fun getForecastWeather(
        location: String,
        days: Int,
        aqi: Boolean,
        alerts: Boolean
    ): Response<ForecastDTO> {
        if (enableError) errorResponse
        else
    }

//    override suspend fun searchLocation(cityName: String): Response<List<SearchDTO>> {
//
//    }
}