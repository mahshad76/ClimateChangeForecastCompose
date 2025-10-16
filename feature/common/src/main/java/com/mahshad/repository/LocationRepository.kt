package com.mahshad.repository

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLocationUpdates(): Flow<Location>
    suspend fun getLastKnownLocation(): Location?
    fun isLocationEnabled(): Flow<Boolean>
    fun hasLocationPermissions(): Boolean
}