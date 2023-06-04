package com.antoniomy.domain.di

import com.antoniomy.data.repository.RemoteService
import com.antoniomy.domain.DistrictRemoteRepository
import com.antoniomy.domain.DistrictRemoteRepositoryImpl
import com.antoniomy.domain.PoiLocalRepository
import com.antoniomy.domain.PoiLocalRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainDI {

    @Provides
    @Singleton
    fun remoteService(retrofit: Retrofit): RemoteService = retrofit.create(RemoteService::class.java)

    @Provides
    @Singleton
    fun providesDistrictRemoteRepo (remoteService: RemoteService): DistrictRemoteRepository = DistrictRemoteRepositoryImpl(remoteService)

    @Provides
    @Singleton
    fun providesPoiLocalRepo (): PoiLocalRepository = PoiLocalRepositoryImpl()
}