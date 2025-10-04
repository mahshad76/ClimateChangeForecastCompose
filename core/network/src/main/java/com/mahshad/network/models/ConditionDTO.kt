package com.mahshad.network.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConditionDTO(
    @SerialName("code") val code: Int? = null,
    @SerialName("icon") val icon: String? = null,
    @SerialName("text") val text: String? = null
) {
    companion object {
        val DEFAULT = ConditionDTO(
            0,
            "icon",
            "text"
        )
    }
}