package com.antoniomy.citypoi.di

import com.antoniomy.citypoi.common.PoiProvider
import com.antoniomy.citypoi.common.PoiProviderImpl
import com.antoniomy.citypoi.navigation.CitiesNavigation
import com.antoniomy.citypoi.navigation.CitiesNavigationImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UiDI {
    @Provides
    @Singleton
    fun citiesNavigation(): CitiesNavigation = CitiesNavigationImpl()

    @Provides
    @Singleton
    fun poiProvider(): PoiProvider = PoiProviderImpl()
}