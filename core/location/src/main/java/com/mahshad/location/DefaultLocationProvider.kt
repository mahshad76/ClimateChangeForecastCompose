package com.mahshad.location

import android.content.Context
import android.location.Location
import android.location.LocationManager
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultLocationProvider @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val locationManager: LocationManager,
    @ApplicationContext private val context: Context
) : LocationProvider {

    override fun getLocationUpdates(): Flow<Location> {
        TODO("Not yet implemented")
    }

    override fun getLastKnownLocation(): Location? {
        TODO("Not yet implemented")
    }

    override fun locationEnabledUpdates(): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override fun hasLocationPermissions(): Boolean {
        TODO("Not yet implemented")
    }
}