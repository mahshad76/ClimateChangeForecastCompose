package com.mahshad.datasource.di

import com.mahshad.datasource.remote.DefaultWeatherDataSource
import com.mahshad.datasource.remote.WeatherDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Singleton
    @Binds
    abstract fun provideWeatherDataSource(defaultWeatherDataSource: DefaultWeatherDataSource): WeatherDataSource
}