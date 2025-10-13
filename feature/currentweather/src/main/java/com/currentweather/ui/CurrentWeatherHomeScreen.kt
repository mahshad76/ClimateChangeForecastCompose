package com.currentweather.ui

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CurrentWeatherHomeScreen(
    viewModel: CurrentWeatherHomeScreenViewModel = hiltViewModel<CurrentWeatherHomeScreenViewModel>(),
    onNavigateToForecastGraph: (String) -> Unit
) {
    val weatherUIState by viewModel.weatherUIState.collectAsStateWithLifecycle()
    val weatherUIData by viewModel.weatherUIData.collectAsStateWithLifecycle()
    val searchLocation by viewModel.searchLocation.collectAsStateWithLifecycle()
    val searchLocationResults by viewModel.searchLocationResults.collectAsStateWithLifecycle()
    val locationPermissionGranted by viewModel.locationPermissionGranted.collectAsStateWithLifecycle()
    val locationEnabled by viewModel.locationEnabled.collectAsStateWithLifecycle()
    val requestLocationPermissions by viewModel.requestLocationPermissions.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    val appBarColor = colorResource(id = weatherUIData.backgroundColorResource)

    LaunchedEffect(Unit) {
        viewModel.startLocationWeatherUpdates()
    }

    LaunchedEffect(requestLocationPermissions) {
        if (requestLocationPermissions) {
            locationPermissionsState.launchMultiplePermissionRequest()
            viewModel.permissionRequestHandled()
        }
    }

    LaunchedEffect(locationPermissionsState.allPermissionsGranted) {
        if (locationPermissionsState.allPermissionsGranted) {
            viewModel.onLocationPermissionsGranted()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.observeSearchLocation()
    }

//    viewModel.getCurrentWeather(
//        "London",
//        api = "no"
//    )
//    viewModel.getForecast("London", 1, false, false)

    //onNavigateToForecastGraph.invoke("London")
    //Text("current weather screen")
}