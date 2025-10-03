package com.mahshad.datasource.model.currentweather

import com.mahshad.network.models.ConditionDTO

data class Condition(
    val code: Int,
    val icon: String,
    val text: String
)

fun ConditionDTO.toCondition(): Condition {
    return Condition(
        code = code ?: 0,
        icon = icon.orEmpty(),
        text = text.orEmpty()
    )
}
