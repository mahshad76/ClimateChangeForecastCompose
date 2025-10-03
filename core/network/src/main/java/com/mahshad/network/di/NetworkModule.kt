package com.mahshad.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mahshad.network.ApiService
import com.mahshad.network.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideApiKeyInterceptor(): ApiKeyInterceptor =
        ApiKeyInterceptor(BuildConfig.API_KEY)

    @Provides
    @Singleton
    fun provideOkhttpClient(
        apiKeyInterceptor: ApiKeyInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(apiKeyInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideJson() = Json {
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = true
        coerceInputValues = true
        encodeDefaults = false
    }

    @Provides
    @Singleton
    fun provideRetrofitConverter(json: Json): Converter.Factory {
        val contentType = "application/json".toMediaType()
        return json.asConverterFactory(contentType)
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, converterFactory: Converter.Factory): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)
}