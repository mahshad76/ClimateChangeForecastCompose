package com.mahshad.datasource.model.currentweather

import com.mahshad.network.models.LocationDTO

data class Location(
    val country: String,
    val lat: Double,
    val localtime: String,
    val long: Double,
    val name: String,
    val region: String
)

fun LocationDTO.toLocation(): Result<Location> {
    return runCatching {
        Location(
            country = country
                ?: throw IllegalStateException("Country is a mandatory field and cannot be null"),
            lat = lat
                ?: throw IllegalStateException("Latitude is a mandatory field and cannot be null"),
            localtime = localtime.orEmpty(),
            long = lon
                ?: throw IllegalStateException("Longitude is a mandatory field and cannot be null"),
            name = name.orEmpty(),
            region = region.orEmpty(),
        )
    }
}
