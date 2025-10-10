package com.mahshad.datasource.model.search

import com.mahshad.network.models.search.SearchDTO

data class Search(
    var id: Int,
    var name: String,
    var region: String,
    var country: String,
    var lat: Double,
    var lon: Double,
    var url: String
)

fun List<SearchDTO>.toSearchResult(): Result<List<Search>> {
    return runCatching {
        this.map { it ->
            Search(
                id = it.id ?: 0,
                name = it.name.orEmpty(),
                region = it.region.orEmpty(),
                country = it.country.orEmpty(),
                lat = it.lat ?: 0.0,
                lon = it.lon ?: 0.0,
                url = it.url.orEmpty()
            )
        }
    }
}

