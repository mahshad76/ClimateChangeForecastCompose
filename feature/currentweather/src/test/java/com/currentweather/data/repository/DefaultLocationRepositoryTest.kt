package com.currentweather.data.repository

import com.mahshad.location.LocationProvider
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Before

class DefaultLocationRepositoryTest {
    @MockK
    private lateinit var locationProvider: LocationProvider
    private lateinit var locationRepository: LocationRepository

    @Before
    fun setup() {

    }

    @After
    fun tearDown() {
    }
}