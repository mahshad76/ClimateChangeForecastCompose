package com.currentweather.data.repository

import com.mahshad.common.model.datasource.models.search.Search

interface SearchRepository {
    suspend fun searchLocation(cityName: String): Result<List<Search>>
}