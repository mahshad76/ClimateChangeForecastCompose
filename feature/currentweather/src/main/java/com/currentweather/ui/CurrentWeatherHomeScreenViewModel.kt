package com.currentweather.ui

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.currentweather.data.repository.CurrentWeatherRepository
import com.currentweather.data.repository.ForecastRepository
import com.currentweather.data.repository.LocationRepository
import com.currentweather.data.repository.SearchRepository
import com.mahshad.common.R
import com.mahshad.common.model.error.RepositoryError
import com.mahshad.datasource.model.currentweather.CurrentWeather
import com.mahshad.datasource.model.forecast.Forecast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

//TODO(lines 40 to 43 written in an update function)
@HiltViewModel
class CurrentWeatherHomeScreenViewModel @Inject constructor(
    private val currentWeatherRepository: CurrentWeatherRepository,
    private val forecastRepository: ForecastRepository,
    private val locationRepository: LocationRepository,
    private val searchRepository: SearchRepository,
) : ViewModel() {

    private val _locationPermissionGranted = MutableStateFlow<Boolean>(false)
    val locationPermissionGranted = _locationPermissionGranted.asStateFlow()

    private val _locationEnabled = MutableStateFlow<Boolean>(false)
    val locationEnabled = _locationEnabled.asStateFlow()

    private val _requestLocationPermissions = MutableStateFlow<Boolean>(false)
    val requestLocationPermissions = _requestLocationPermissions.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _searchLocationResults = MutableStateFlow<List<String>>(emptyList())
    val searchLocationResults = _searchLocationResults.asStateFlow()

    private val _searchLocation = MutableStateFlow<String>("")
    val searchLocation = _searchLocation.asStateFlow()

    private val _weatherUIState = MutableStateFlow<WeatherUIState>(WeatherUIState.Idle)
    val weatherUIState: StateFlow<WeatherUIState> = _weatherUIState

    private val _weatherUIData = MutableStateFlow(getWeatherUI(""))
    val weatherUIData = _weatherUIData.asStateFlow()

    fun updateLocationPermissionGranted(value: Boolean) {
        _locationPermissionGranted.update {
            value
        }
    }

    fun updateLocationEnabled(value: Boolean) {
        _locationEnabled.update {
            value
        }
    }

    fun updateRequestLocationPermissions(value: Boolean) {
        _requestLocationPermissions.update {
            value
        }
    }

    fun observeSearchLocation() {
        _searchLocation
            .debounce(300)
            .distinctUntilChanged()
            .onEach { cityName ->
                if (cityName.isNotBlank()) {
                    searchLocationApiCall(cityName)
                } else {
                    _searchLocationResults.value = emptyList()
                }
            }
            .launchIn(viewModelScope)
    }

    fun updateSearchLocation(cityName: String) {
        _searchLocation.value = cityName
    }

    fun searchLocationApiCall(cityName: String) {
        viewModelScope.launch {
            val result = searchRepository.searchLocation(cityName)
            result.onSuccess { locations ->
                _searchLocationResults.value = locations.map { "${it.name}, ${it.country}" }

            }.onFailure {
                _searchLocationResults.value = emptyList()
            }
        }
    }

    private fun extractCityName(formattedLocationString: String): String {
        return formattedLocationString.split(",").firstOrNull()?.trim() ?: formattedLocationString
    }

    fun handleSearchResultClick(formattedLocationString: String) {
        val cityName = extractCityName(formattedLocationString)
        updateSearchLocation(cityName)
        _searchLocationResults.value = emptyList()
        fetchWeatherOnLocation()
    }

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
                fetchWeatherOnLocation()
            }
        }.launchIn(viewModelScope)
    }

    fun startLocationWeatherUpdates() {
        trackLocationEnabledStatus()
        checkLocationPermission()
        fetchWeatherOnPermissionChange()
    }

    @Suppress("UNCHECKED_CAST")
    private fun fetchWeatherOnLocation(isRefreshing: Boolean = false) {
        if (!isRefreshing && _searchLocation.value.isEmpty() && _weatherUIState.value is WeatherUIState.Success) {
            return
        }

        if (isRefreshing) {
            _isRefreshing.value = true
        } else {
            _weatherUIState.value = WeatherUIState.Loading
        }

        viewModelScope.launch {
            try {
                val locationString = _searchLocation.value.ifEmpty {
                    val location = locationRepository.getLocationUpdates().first()
                    "${location.latitude},${location.longitude}"
                }
                val (currentWeather, forecast) = coroutineScope {
                    val currentWeatherDeferred =
                        async {
                            currentWeatherRepository.getCurrentWeather(
                                locationString, "no"
                            )
                        }
                    val forecastDeferred =
                        async {
                            forecastRepository.getForecast(
                                locationString, 1, false, false
                            )
                        }

                    currentWeatherDeferred.await().getOrThrow() to forecastDeferred.await()
                        .getOrThrow()
                }

                _weatherUIState.value = WeatherUIState.Success(
                    currentWeather = currentWeather,
                    forecast = forecast
                )

                _weatherUIData.value = getWeatherUI(currentWeather.current.condition.text)
            } catch (e: Exception) {
                val errorType = when (e) {
                    is RepositoryError.NetworkError -> ErrorType.NetworkError
                    is RepositoryError.NoDataError -> ErrorType.NoDataError
                    is RepositoryError.MappingError -> ErrorType.MappingError
                    else -> ErrorType.UnknownError
                }
                _weatherUIState.value = WeatherUIState.Error(errorType = errorType)
            } finally {
                if (isRefreshing) {
                    _isRefreshing.value = false
                }
            }
        }
    }

    private fun getWeatherUI(text: String): WeatherUI {
        return when {
            text.lowercase().contains("sun") ->
                WeatherUI(
                    backgroundImageResource = R.drawable.ic_sunny,
                    backgroundColorResource = R.color.light_beige
                )

            text.lowercase().contains("cloud") || text.lowercase().contains("wind") ->
                WeatherUI(
                    backgroundImageResource = R.drawable.ic_cloudy,
                    backgroundColorResource = R.color.pale_blue
                )

            text.lowercase().contains("rain") || text.lowercase()
                .contains("drizzle") || text.lowercase()
                .contains("snow") ->
                WeatherUI(
                    backgroundImageResource = R.drawable.ic_rainy,
                    backgroundColorResource = R.color.dark_shade_blue,
                    textColorResource = R.color.white
                )

            else ->
                WeatherUI(
                    R.drawable.ic_cloudy, R.color.pale_blue
                )
        }
    }

    fun onLocationPermissionsGranted() {
        _locationPermissionGranted.value = true
        _requestLocationPermissions.value = false
    }

    fun permissionRequestHandled() {
        _requestLocationPermissions.value = false
    }

    fun retryFetchWeather(isRefreshing: Boolean = false) {
        fetchWeatherOnLocation(isRefreshing = isRefreshing)
    }
}

data class WeatherUI(
    @DrawableRes val backgroundImageResource: Int,
    @ColorRes val backgroundColorResource: Int,
    @ColorRes val textColorResource: Int = R.color.black
)

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