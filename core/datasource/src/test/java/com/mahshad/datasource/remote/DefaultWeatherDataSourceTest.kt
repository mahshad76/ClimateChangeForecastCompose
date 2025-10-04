package com.mahshad.datasource.remote

import com.mahshad.network.ApiService
import org.junit.After
import org.junit.Before
import org.junit.Test

class DefaultWeatherDataSourceTest {
    private lateinit var apiService: ApiService
    private lateinit var weatherDataSource: WeatherDataSource
    // rule to displace the real dispatchers

    @Before
    fun setup() {
        apiService = ApiServiceFake()
    }

    @Test


    @After
    fun teardown() {

    }
}