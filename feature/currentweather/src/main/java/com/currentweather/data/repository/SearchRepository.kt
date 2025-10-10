package com.currentweather.data.repository

import com.mahshad.datasource.model.search.Search

interface SearchRepository {
    suspend fun searchLocation(cityName: String): Result<List<Search>>
}