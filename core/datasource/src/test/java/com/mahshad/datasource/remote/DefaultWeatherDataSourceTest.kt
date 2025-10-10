package com.mahshad.datasource.remote

import com.mahshad.network.ApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class DefaultWeatherDataSourceTest {
    private lateinit var testScheduler: TestCoroutineScheduler
    private lateinit var testDispatcher: CoroutineDispatcher
    private lateinit var apiService: ApiService
    private lateinit var weatherDataSource: WeatherDataSource

    @Before
    fun setup() {
        testScheduler = TestCoroutineScheduler()
        testDispatcher = StandardTestDispatcher(testScheduler)
        apiService = ApiServiceFake()
        weatherDataSource = DefaultWeatherDataSource(apiService, testDispatcher)
    }

    @Test
    fun `getCurrentWeather_withUnsuccessfulResponse_returnsFailureResult`() =
        runTest(testScheduler) {
            // Given
            apiService.enableError = true
            // When
            val result = weatherDataSource.getCurrentWeather("London", "no")
            // Then
            assertEquals(result.isSuccess, false)
            if (result.isFailure) {
                assertEquals(
                    "the response body is not successful",
                    result.exceptionOrNull()?.message
                )
            }
        }

//    @Test
//    fun `getCurrentWeather_withNullSuccessfulResponse_returnsFailureResult`() =
//        runTest(testScheduler) {
//
//        }

    @Test
    fun `getCurrentWeather_withSuccessfulResponse_returnsSuccessfulResult`() =
        runTest(testScheduler) {
            // When
            val result = weatherDataSource.getCurrentWeather("London", "no")
            // Then
            assertEquals(result.isSuccess, true)
        }

    @Test
    fun `getCurrentWeather_withNullParameter_returnsFailureResult`() = runTest(testScheduler) {
        // Given
        apiService.nullifyForbiddenAttributes = true
        // When
        val result = weatherDataSource.getCurrentWeather("London", "no")
        // Then
        assertEquals(result.isSuccess, false)
        if (result.isFailure) {
            assertEquals(
                "CurrentDTO is missing for CurrentWeather and cannot be null.",
                result.exceptionOrNull()?.message
            )
        }
    }


    @Test
    fun `getForecastWeather_withUnsuccessfulResponse_returnsFailureResult`() =
        runTest(testScheduler) {
            // Given
            apiService.enableError = true
            // When
            val result = weatherDataSource.getForecastWeather(
                "London", 1, false, false
            )
            // Then
            assertEquals(result.isSuccess, false)
            if (result.isFailure) {
                assertEquals(
                    "the response body is not successful",
                    result.exceptionOrNull()?.message
                )
            }
        }

    @Test
    fun `getForecastWeather_withSuccessfulResponse_returnsSuccessfulResult`() =
        runTest(testScheduler) {
            // When
            val result = weatherDataSource.getForecastWeather(
                "London", 1, false, false
            )
            // Then
            assertEquals(result.isSuccess, true)
        }

    @Test
    fun `getForecastWeather_withNullParameter_returnsFailureResult`() = runTest(testDispatcher) {
        // Given
        apiService.nullifyForbiddenAttributes = true
        // When
        val result = weatherDataSource.getForecastWeather(
            "London", 1, false, false
        )
        // Then
        assertEquals(result.isSuccess, false)
        if (result.isFailure) {
            assertEquals(
                "CurrentDTO is missing for Forecast and cannot be null.",
                result.exceptionOrNull()?.message
            )
        }
    }
}