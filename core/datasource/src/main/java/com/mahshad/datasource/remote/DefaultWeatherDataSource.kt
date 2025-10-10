package com.mahshad.datasource.remote

import com.mahshad.datasource.model.currentweather.CurrentWeather
import com.mahshad.datasource.model.currentweather.toCurrentWeather
import com.mahshad.datasource.model.forecast.Forecast
import com.mahshad.datasource.model.forecast.toForecastResult
import com.mahshad.datasource.model.search.Search
import com.mahshad.datasource.model.search.toSearchResult
import com.mahshad.network.ApiService
import com.mahshad.threading.common.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultWeatherDataSource @Inject constructor(
    private val apiService: ApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : WeatherDataSource {

    override suspend fun getCurrentWeather(
        q: String,
        api: String
    ): Result<CurrentWeather> {
        // Main safe
        return withContext(ioDispatcher) {
            try {
                val response = apiService.getCurrentWeather(q, api)
                when (response.isSuccessful) {
                    true -> {
                        return@withContext response.body()?.let {
                            it.toCurrentWeather().fold(
                                onSuccess = { currentWeather -> Result.success(currentWeather) },
                                onFailure = { throwable -> Result.failure(throwable) }
                            )
                        }
                            ?: return@withContext Result.failure(Throwable("Successful response but body was unexpectedly null"))
                    }

                    false -> return@withContext Result.failure(Throwable("the response body is not successful"))
                }
            } catch (e: Exception) {
                return@withContext Result.failure(e)
            }
        }
    }

    override suspend fun getForecastWeather(
        location: String,
        days: Int,
        aqi: Boolean,
        alerts: Boolean
    ): Result<Forecast> {
        return withContext(ioDispatcher) {
            try {
                val response = apiService.getForecastWeather(location, days, aqi, alerts)
                when (response.isSuccessful) {
                    true -> {
                        return@withContext response.body()?.let {
                            it.toForecastResult().fold(
                                onSuccess = { forecast -> Result.success(forecast) },
                                onFailure = { throwable -> Result.failure(throwable) }
                            )
                        }
                            ?: return@withContext Result.failure(Throwable("Successful response but body was unexpectedly null"))
                    }

                    false -> return@withContext Result.failure(Throwable("the response body is not successful"))
                }
            } catch (e: Exception) {
                return@withContext Result.failure(e)
            }
        }
    }

    override suspend fun searchLocation(cityName: String): Result<List<Search>> {
        return withContext(ioDispatcher) {
            try {
                val response = apiService.searchLocation(cityName)
                when (response.isSuccessful) {
                    true -> {
                        return@withContext response.body()?.let {
                            it.toSearchResult().fold(
                                onSuccess = { searchList -> Result.success(searchList) },
                                onFailure = { throwable -> Result.failure(throwable) }
                            )
                        }
                            ?: return@withContext Result.failure(Throwable("Successful response but body was unexpectedly null"))
                    }

                    false -> return@withContext Result.failure(Throwable("the response body is not successful"))
                }
            } catch (e: Exception) {
                return@withContext Result.failure(e)
            }
        }
    }
}