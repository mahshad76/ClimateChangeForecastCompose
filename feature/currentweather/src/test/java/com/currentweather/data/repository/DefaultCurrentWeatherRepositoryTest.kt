package com.currentweather.data.repository

import com.mahshad.datasource.data.currentweather.CurrentWeather
import com.mahshad.datasource.remote.WeatherDataSource
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DefaultCurrentWeatherRepositoryTest {
    @MockK
    private lateinit var weatherDataSource: WeatherDataSource
    private lateinit var currentWeatherRepository: CurrentWeatherRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        currentWeatherRepository = DefaultCurrentWeatherRepository(weatherDataSource)
    }

    @Test
    fun `getCurrentWeather_nullBodyFailure_returningFailureResult`() = runTest {
        // Given
        coEvery {
            weatherDataSource.getCurrentWeather(
                q = "",
                api = ""
            )
        } returns Result.failure(
            Throwable(
                "Successful response but body was unexpectedly null"
            )
        )
        // When
        val result = currentWeatherRepository.getCurrentWeather(
            q = "",
            api = ""
        )
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
    fun `getCurrentWeather_nullPropertiesFailure_returningFailureResult`() = runTest {
        // Given
        coEvery {
            weatherDataSource.getCurrentWeather(
                q = "",
                api = ""
            )
        } returns Result.failure(
            Throwable(
                "Unsuccessful response"
            )
        )
        // When
        val result = currentWeatherRepository.getCurrentWeather(
            q = "",
            api = ""
        )
        // Then
        assertEquals(true, result.isFailure)
    }

    @Test
    fun `getCurrentWeather_responseErrorFailure_returningFailureResult`() = runTest {
        // Given
        coEvery {
            weatherDataSource.getCurrentWeather(
                q = "",
                api = ""
            )
        } returns Result.failure(
            Throwable("the response body is not successful")
        )
        // When
        val result = currentWeatherRepository.getCurrentWeather(
            q = "",
            api = ""
        )
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
    fun `getCurrentWeather_successfulResponse_returningSuccessfulResult`() = runTest {
        // Given
        coEvery {
            weatherDataSource.getCurrentWeather(
                q = "",
                api = ""
            )
        } returns Result.success(CurrentWeather.DEFAULT)
        // When
        val result = currentWeatherRepository.getCurrentWeather(
            q = "",
            api = ""
        )
        // Then
        assertEquals(true, result.isSuccess)
        if (result.isSuccess) {
            assertEquals(
                result.getOrNull(),
                CurrentWeather.DEFAULT
            )
        }
    }

//    @Test
//    fun `getCurrentWeather_dataSourceResponseIsNull_returningFailureResult`(){
//    }
//
//    @Test
//    fun `getCurrentWeather_dataSourceResponseIsNull_returningFailureResult`(){
//    }

    @After
    fun teardown() {
        clearAllMocks()
    }
}