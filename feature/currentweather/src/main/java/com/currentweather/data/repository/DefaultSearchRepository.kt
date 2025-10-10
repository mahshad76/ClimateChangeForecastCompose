package com.currentweather.data.repository

import com.mahshad.datasource.model.search.Search
import com.mahshad.datasource.remote.WeatherDataSource
import javax.inject.Inject

class DefaultSearchRepository @Inject constructor(
    private val weatherDataSource: WeatherDataSource
) : SearchRepository {
    override suspend fun searchLocation(cityName: String): Result<List<Search>> {
        return weatherDataSource.searchLocation(
            cityName
        )
    }
}