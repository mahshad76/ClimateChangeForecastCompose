package com.currentweather.data.repository

import com.mahshad.datasource.remote.WeatherDataSource
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class DefaultCurrentWeatherRepositoryTest {
    @Mock
    private lateinit var weatherDataSource: WeatherDataSource
    private lateinit var closeable: AutoCloseable
    private lateinit var currentWeatherRepository: CurrentWeatherRepository

    @Before
    fun setup() {
        closeable = MockitoAnnotations.openMocks(this)
        currentWeatherRepository = DefaultCurrentWeatherRepository(weatherDataSource)
    }

    @Test
    fun `getCurrentWeather_nullBodyFailure_returningFailureResult`() {
        // Given
        `when`(weatherDataSource.getCurrentWeather()).thenReturn(someReturnValue)
        doReturn("Simulated User").`when`(weatherDataSource.getCurrentWeather())
    }

    @Test
    fun `getCurrentWeather_nullPropertiesFailure_returningFailureResult`() {

    }

    @Test
    fun `getCurrentWeather_responseErrorFailure_returningFailureResult`() {

    }

    @Test
    fun `getCurrentWeather_successfulResponse_returningSuccessfulResult`() {

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
        closeable.close()
    }
}