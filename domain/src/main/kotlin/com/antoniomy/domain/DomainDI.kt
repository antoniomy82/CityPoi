package com.antoniomy.domain

import com.antoniomy.data.service.RemoteService
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
    fun providesDistrictRemoteRepo (remoteService: RemoteService): DistrictRemote = DistrictRemoteImpl(remoteService)
}