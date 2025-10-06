package com.mahshad.common

sealed interface Response<out T> {
    data class Successful<T>(val successfulResult: T) : Response<T>
    data class Error(val exception: Exception) : Response<Nothing>
    data object Loading : Response<Nothing>
}