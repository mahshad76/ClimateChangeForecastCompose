package com.currentweather.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currentweather.data.repository.CurrentWeatherRepository
import com.mahshad.common.Response
import com.mahshad.datasource.model.currentweather.CurrentWeather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherHomeScreenViewModel @Inject constructor(
    private val currentWeatherRepository: CurrentWeatherRepository
) : ViewModel() {
    private val _currentWeatherUiState = MutableStateFlow<Response<CurrentWeather>>(
        Response.Loading
    )
    val currentWeatherUiState: StateFlow<Response<CurrentWeather>> = _currentWeatherUiState

    fun getCurrentWeather(q: String, api: String) {
        viewModelScope.launch {
            val result = currentWeatherRepository.getCurrentWeather(q, api)
            when (result.isSuccess) {
                true -> Log.d("TAG", "getCurrentWeather: ${result.getOrNull()}")
                false -> Log.d("TAG", "error: ${result.exceptionOrNull()}")
            }
        }
    }
}