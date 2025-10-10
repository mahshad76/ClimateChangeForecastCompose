package com.currentweather.data.repository

import android.location.Location
import com.mahshad.location.LocationProvider
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultLocationRepository @Inject constructor(
    private val locationProvider: LocationProvider
) : LocationRepository {
    override fun getLocationUpdates(): Flow<Location> = locationProvider.getLocationUpdates()

    override suspend fun getLastKnownLocation(): Location? = locationProvider.getLastKnownLocation()

    override fun isLocationEnabled(): Flow<Boolean> = locationProvider.isLocationEnabled()

    override fun hasLocationPermissions(): Boolean = locationProvider.hasLocationPermissions()
}