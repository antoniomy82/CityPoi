package com.antoniomy.domain.di

import com.antoniomy.data.repository.remote.RemoteService
import com.antoniomy.domain.datasource.local.LocalDb
import com.antoniomy.domain.datasource.remote.RemoteRepository
import com.antoniomy.domain.datasource.remote.RemoteRepositoryImpl
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
    fun providesDistrictRemoteRepo (remoteService: RemoteService): RemoteRepository = RemoteRepositoryImpl(remoteService)

    @Provides
    @Singleton
    fun providesLocalRepo (): LocalDb= LocalDb()

}