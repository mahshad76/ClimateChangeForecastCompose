package com.currentweather.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currentweather.data.repository.CurrentWeatherRepository
import com.currentweather.data.repository.ForecastRepository
import com.currentweather.data.repository.LocationRepository
import com.currentweather.data.repository.SearchRepository
import com.mahshad.datasource.model.currentweather.CurrentWeather
import com.mahshad.datasource.model.forecast.Forecast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherHomeScreenViewModel @Inject constructor(
    private val currentWeatherRepository: CurrentWeatherRepository,
    private val forecastRepository: ForecastRepository,
    private val locationRepository: LocationRepository,
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _locationPermissionGranted = MutableStateFlow(false)
    val locationPermissionGranted = _locationPermissionGranted.asStateFlow()

    private val _locationEnabled = MutableStateFlow(false)
    val locationEnabled = _locationEnabled.asStateFlow()

    private val _requestLocationPermissions = MutableStateFlow(false)
    val requestLocationPermissions = _requestLocationPermissions.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _searchLocationResults = MutableStateFlow<List<String>>(emptyList())
    val searchLocationResults = _searchLocationResults.asStateFlow()

    private val _weatherUIState = MutableStateFlow<WeatherUIState>(WeatherUIState.Idle)
    val weatherUIState: StateFlow<WeatherUIState> = _weatherUIState

    private fun checkLocationPermission() {
        val hasPermission = locationRepository.hasLocationPermissions()
        _locationPermissionGranted.value = hasPermission
        if (!hasPermission) {
            _requestLocationPermissions.value = true
            _weatherUIState.value =
                WeatherUIState.Error(errorType = ErrorType.LocationPermissionDenied)
        }
    }

    private fun trackLocationEnabledStatus() {
        locationRepository.isLocationEnabled()
            .onEach { isEnabled ->
                _locationEnabled.value = isEnabled
                if (!isEnabled) {
                    _weatherUIState.value =
                        WeatherUIState.Error(errorType = ErrorType.LocationServicesDisabled)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun fetchWeatherOnPermissionChange() {
        combine(locationPermissionGranted, locationEnabled) { permissionGranted, locationEnabled ->
            permissionGranted && locationEnabled
        }.onEach { shouldFetch ->
            if (shouldFetch) {
//                fetchWeatherOnLocation()
            }
        }.launchIn(viewModelScope)
    }

    fun startLocationWeatherUpdates() {
        trackLocationEnabledStatus()
        checkLocationPermission()
        fetchWeatherOnPermissionChange()
    }

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