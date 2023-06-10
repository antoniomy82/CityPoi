package com.antoniomy.domain.di

import android.content.Context
import androidx.room.Room
import com.antoniomy.data.repository.RemoteService
import com.antoniomy.domain.datasource.local.LocalRepository
import com.antoniomy.domain.datasource.local.LocalRepositoryImpl
import com.antoniomy.domain.datasource.local.PoiDB
import com.antoniomy.domain.datasource.remote.RemoteRepository
import com.antoniomy.domain.datasource.remote.RemoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun providesDistrictRemoteRepo(remoteService: RemoteService): RemoteRepository = RemoteRepositoryImpl(remoteService)

    @Singleton
    @Provides
    fun provideLocalDb(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(appContext, PoiDB::class.java, "PoiLocalDB").fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun providePoiDao(db: PoiDB): LocalRepository = LocalRepositoryImpl(db.getPoiDAO())
}