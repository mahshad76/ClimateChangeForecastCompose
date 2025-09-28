package com.mahshad.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationProvider {
    fun getLocationUpdates(): Flow<Location>
    fun getLastKnownLocation(): Location?
    fun locationEnabledUpdates(): Flow<Boolean>
    fun hasLocationPermissions(): Boolean
}