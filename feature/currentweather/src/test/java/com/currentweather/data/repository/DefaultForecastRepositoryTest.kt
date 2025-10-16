package com.currentweather.data.repository

import com.mahshad.common.model.datasource.models.forecast.Forecast
import com.mahshad.datasource.remote.WeatherDataSource
import com.mahshad.repository.DefaultForecastRepository
import com.mahshad.repository.ForecastRepository
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DefaultForecastRepositoryTest {
    @MockK
    private lateinit var weatherDataSource: WeatherDataSource
    private lateinit var forecastRepository: ForecastRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        forecastRepository = DefaultForecastRepository(weatherDataSource)
    }

    @Test
    fun `getForecast_nullBodyFailure_returningFailureResult`() = runTest {
        // Given
        coEvery {
            weatherDataSource.getForecastWeather(
                "London",
                1,
                false,
                false
            )
        } returns Result.failure(
            Throwable(
                "Successful response but body was unexpectedly null"
            )
        )
        // When
        val result = forecastRepository.getForecast("London", 1, false, false)
        // Then
        assertEquals(true, result.isFailure)
        if (result.isFailure) {
            assertEquals(
                result.exceptionOrNull()?.message,
                "Successful response but body was unexpectedly null"
            )
        }
    }

    @Test
    fun `getForecast_nullPropertiesFailure_returningFailureResult`() = runTest {
        // Given
        coEvery {
            weatherDataSource.getForecastWeather(
                "London",
                1,
                false,
                false
            )
        } returns Result.failure(
            Throwable(
                "Unsuccessful response"
            )
        )
        // When
        val result = forecastRepository.getForecast("London", 1, false, false)
        // Then
        assertEquals(true, result.isFailure)
    }

    @Test
    fun `getForecast__responseErrorFailure_returningFailureResult`() = runTest {
        // Given
        coEvery {
            weatherDataSource.getForecastWeather(
                "London",
                1,
                false,
                false
            )
        } returns Result.failure(
            Throwable(
                "the response body is not successful"
            )
        )
        // When
        val result = forecastRepository.getForecast("London", 1, false, false)
        // Then
        assertEquals(true, result.isFailure)
        if (result.isFailure) {
            assertEquals(
                result.exceptionOrNull()?.message,
                "the response body is not successful"
            )
        }
    }

    @Test
    fun `getForecast_successfulResponse_returningSuccessfulResult`() = runTest {
        // Given
        coEvery {
            weatherDataSource.getForecastWeather(
                "London",
                1,
                false,
                false
            )
        } returns Forecast.DEFAULT
        // When
        val result = forecastRepository.getForecast("London", 1, false, false)
        // Then
        assertEquals(true, result.isSuccess)
        if (result.isSuccess) {
            assertEquals(
                result.getOrNull(),
                Forecast.DEFAULT.getOrNull()
            )
        }

    }

    @After
    fun teardown() {
        clearAllMocks()
    }
}