package com.currentweather.di

import com.mahshad.repository.CurrentWeatherRepository
import com.mahshad.repository.DefaultCurrentWeatherRepository
import com.mahshad.repository.DefaultForecastRepository
import com.mahshad.repository.DefaultLocationRepository
import com.mahshad.repository.DefaultSearchRepository
import com.mahshad.repository.ForecastRepository
import com.mahshad.repository.LocationRepository
import com.mahshad.repository.SearchRepository
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