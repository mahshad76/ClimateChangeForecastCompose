package com.mahshad.common.model.network.models.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchDTO(
    @SerialName("id") var id: Int? = null,
    @SerialName("name") var name: String? = null,
    @SerialName("region") var region: String? = null,
    @SerialName("country") var country: String? = null,
    @SerialName("lat") var lat: Double? = null,
    @SerialName("lon") var lon: Double? = null,
    @SerialName("url") var url: String? = null
) {
    companion object {
        val DEFAULT = SearchDTO(
            id = 0,
            name = "",
            region = "",
            country = "",
            lon = 0.0,
            lat = 0.0,
            url = ""
        )
    }
}