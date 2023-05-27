package com.antoniomy.data.di

import androidx.multidex.BuildConfig
import com.antoniomy.data.repository.DistrictRemoteRepo
import com.antoniomy.data.repository.DistrictRemoteRepoImpl
import com.antoniomy.data.api.ApiService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

const val urlCities = "https://cityme-services.prepro.site/app_dev.php/api/districts/"
const val timeOut: Long = 60

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesOkhttp():OkHttpClient{
        val okHttp = OkHttpClient.Builder()
            .connectTimeout(timeOut, TimeUnit.SECONDS)
            .readTimeout(timeOut, TimeUnit.SECONDS)
            .writeTimeout(timeOut, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val logger = HttpLoggingInterceptor()
            logger.setLevel(HttpLoggingInterceptor.Level.BODY)

            okHttp.addInterceptor(logger)
        }
        return okHttp.build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient):Retrofit = Retrofit
        .Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(okHttpClient)
        .baseUrl(urlCities)
        .build()

    @Provides
    @Singleton
    fun apiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun providesCharactersRepo(apiService: ApiService): DistrictRemoteRepo = DistrictRemoteRepoImpl(apiService)

}