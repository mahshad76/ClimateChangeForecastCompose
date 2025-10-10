package com.currentweather.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currentweather.data.repository.CurrentWeatherRepository
import com.currentweather.data.repository.ForecastRepository
import com.currentweather.data.repository.LocationRepository
import com.mahshad.datasource.model.currentweather.CurrentWeather
import com.mahshad.datasource.model.forecast.Forecast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherHomeScreenViewModel @Inject constructor(
    private val currentWeatherRepository: CurrentWeatherRepository,
    private val forecastRepository: ForecastRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val weatherUIState = MutableStateFlow<WeatherUIState>(WeatherUIState.Idle)
    val _weatherUIState: StateFlow<WeatherUIState> = weatherUIState

    fun getCurrentWeather(q: String, api: String) {
        viewModelScope.launch {
            val result = currentWeatherRepository.getCurrentWeather(q, api)
            when (result.isSuccess) {
                true -> Log.d("TAG", "getCurrentWeather: ${result.getOrNull()}")
                false -> Log.d("TAG", "error: ${result.exceptionOrNull()}")
            }
        }
    }

    fun getForecast(
        location: String,
        dates: Int,
        aqi: Boolean,
        api: Boolean
    ) {
        viewModelScope.launch {
            val result = forecastRepository.getForecast(location, dates, aqi, api)
            when (result.isSuccess) {
                true -> Log.d("TAG", "getForecast: ${result.getOrNull()}")
                false -> Log.d("TAG", "error: ${result.exceptionOrNull()}")
            }
        }
    }
}

/**
 * Represents the different states of the weather UI.
 */
sealed interface WeatherUIState {
    /**
     * Represents the idle state of the UI.
     */
    data object Idle : WeatherUIState

    /**
     * Represents the loading state of the UI.
     */
    data object Loading : WeatherUIState

    /**
     * Represents the success state of the UI, containing current weather and forecast data.
     * @param currentWeather The current weather information.
     * @param forecast The forecast information.
     */
    data class Success(
        val currentWeather: CurrentWeather,
        val forecast: Forecast
    ) : WeatherUIState

    /**
     * Represents the error state of the UI.
     * @param errorType The type of error that occurred.
     */
    data class Error(val errorType: ErrorType) : WeatherUIState
}

/**
 * Represents the error state of the UI.
 * @param errorType The type of error that occurred.
 */
sealed interface ErrorType {
    data object LocationServicesDisabled : ErrorType
    data object NetworkError : ErrorType
    data object NoDataError : ErrorType
    data object MappingError : ErrorType
    data object LocationPermissionDenied : ErrorType
    data object UnknownError : ErrorType
}