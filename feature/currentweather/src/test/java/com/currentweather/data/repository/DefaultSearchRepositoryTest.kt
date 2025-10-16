package com.currentweather.data.repository

import com.mahshad.datasource.data.search.Search
import com.mahshad.datasource.remote.WeatherDataSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class DefaultSearchRepositoryTest {
    @MockK
    private lateinit var weatherDataSource: WeatherDataSource
    private lateinit var searchRepository: SearchRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        searchRepository = DefaultSearchRepository(weatherDataSource)
    }

    @Test
    fun `searchLocation_withSuccessfulResult_returnsSuccessfulResult`() =
        runTest() {
            // GIVEN
            coEvery { weatherDataSource.searchLocation(any()) } returns
                    Result.success(listOf(Search.DEFAULT))
            // WHEN
            val result = searchRepository.searchLocation("")
            // THEN
            result.fold(
                {
                    assertEquals(
                        listOf(Search.DEFAULT),
                        it
                    )
                },
                {
                    Throwable("")
                }
            )
        }

    @Test
    fun `searchLocation_withUnsuccessfulResult_returnsFailureResult`() =
        runTest() {
            // GIVEN
            coEvery { weatherDataSource.searchLocation(any()) } returns
                    Result.failure(Throwable("the response body is not successful"))
            // WHEN
            val result = searchRepository.searchLocation("")
            // THEN
            result.fold(
                {
                    Throwable("")
                },
                {
                    assertEquals(
                        "the response body is not successful",
                        it.message
                    )
                }
            )
        }

    @After
    fun tearDown() = unmockkAll()
}