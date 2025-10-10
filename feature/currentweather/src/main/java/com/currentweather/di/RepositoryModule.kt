package com.currentweather.di

import com.currentweather.data.repository.CurrentWeatherRepository
import com.currentweather.data.repository.DefaultCurrentWeatherRepository
import com.currentweather.data.repository.DefaultForecastRepository
import com.currentweather.data.repository.DefaultLocationRepository
import com.currentweather.data.repository.DefaultSearchRepository
import com.currentweather.data.repository.ForecastRepository
import com.currentweather.data.repository.LocationRepository
import com.currentweather.data.repository.SearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindCurrentWeatherRepository(
        defaultCurrentWeatherRepository:
        DefaultCurrentWeatherRepository
    ):
            CurrentWeatherRepository

    @Singleton
    @Binds
    abstract fun bindForecastRepository(defaultForecastRepository: DefaultForecastRepository):
            ForecastRepository

    @Singleton
    @Binds
    abstract fun bindLocationRepository(defaultLocationRepository: DefaultLocationRepository):
            LocationRepository

    @Singleton
    @Binds
    abstract fun bindSearchRepository(defaultSearchRepository: DefaultSearchRepository):
            SearchRepository
}