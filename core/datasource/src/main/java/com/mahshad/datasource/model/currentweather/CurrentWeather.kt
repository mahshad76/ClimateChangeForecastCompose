package com.mahshad.datasource.model.currentweather

import com.mahshad.network.models.currentweather.CurrentWeatherDTO

data class CurrentWeather(
    val current: Current,
    val location: Location
) {
    companion object {
        val DEFAULT = CurrentWeather(Current.DEFAULT, Location.DEFAULT)
    }
}

fun CurrentWeatherDTO.toCurrentWeather(): Result<CurrentWeather> {
    return runCatching {
        val currentDtoNonNull = currentDTO
            ?: throw IllegalArgumentException("CurrentDTO is missing for CurrentWeather and cannot be null.")
        val locationDtoNonNull = locationDTO
            ?: throw IllegalArgumentException("LocationDTO is missing for CurrentWeather and cannot be null.")

        val current = currentDtoNonNull.toCurrent().getOrThrow()
        val location = locationDtoNonNull.toLocation().getOrThrow()

        CurrentWeather(
            current = current,
            location = location
        )
    }
}
