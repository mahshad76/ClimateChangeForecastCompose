package com.mahshad.location.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mahshad.location.LocationProvider
import dagger.Binds
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
abstract class LocationModule {
    @Binds
    @Singleton
    abstract fun bindLocationProvider(): LocationProvider

    companion object {
        @Provides
        @Singleton
        fun provideFusedLocationClient(@ApplicationContext context: Context):
                FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)
    }
}