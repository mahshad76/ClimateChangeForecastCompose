package com.mahshad.datasource.remote

import com.mahshad.datasource.model.currentweather.CurrentWeather
import com.mahshad.datasource.model.currentweather.toCurrentWeather
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
        days: Int,
        api: String,
        alert: String
    ): Result<CurrentWeather> {
        // Main safe
        return withContext(ioDispatcher) {
            try {
                val response = apiService.getCurrentWeather(q, days, api, alert)
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
}