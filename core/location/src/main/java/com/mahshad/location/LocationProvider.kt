package com.mahshad.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationProvider {
    fun getLocationUpdates(): Flow<Location>
    suspend fun getLastKnownLocation(): Location?
    fun isLocationEnabled(): Flow<Boolean>
    fun hasLocationPermissions(): Boolean
}