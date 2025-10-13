package com.currentweather.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MutableStateFlowModule {
    @Provides
    @Singleton
    fun provideSearchLocation() = MutableStateFlow("")

    @Provides
    @Singleton
    @LocationPermissionGranted
    fun provideLocationPermissionGranted() = MutableStateFlow(false)

    @Provides
    @Singleton
    @LocationEnabled
    fun provideLocationEnabled() = MutableStateFlow(false)

    @Provides
    @Singleton
    @LocationPermissions
    fun provideRequestLocationPermissions() = MutableStateFlow(false)
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LocationPermissionGranted

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LocationEnabled

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LocationPermissions