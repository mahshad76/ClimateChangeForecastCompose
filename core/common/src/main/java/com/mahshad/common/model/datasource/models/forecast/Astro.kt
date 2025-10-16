package com.mahshad.common.model.datasource.models.forecast

import com.mahshad.common.model.network.models.forecast.AstroDTO

data class Astro(
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    val moonset: String,
    val moonPhase: String,
    val moonIllumination: Int,
    val isMoonUp: Int,
    val isSunUp: Int,
)

fun AstroDTO.toAstro(): Astro {
    return Astro(
        sunrise = sunrise.orEmpty(),
        sunset = sunset.orEmpty(),
        moonrise = moonrise.orEmpty(),
        moonset = moonset.orEmpty(),
        moonPhase = moonPhase.orEmpty(),
        moonIllumination = moonIllumination ?: 0,
        isMoonUp = isMoonUp ?: 0,
        isSunUp = isSunUp ?: 0,
    )
}