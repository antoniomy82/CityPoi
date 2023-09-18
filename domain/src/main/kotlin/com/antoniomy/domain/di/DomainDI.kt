package com.antoniomy.domain.di

import android.content.Context
import androidx.room.Room
import com.antoniomy.data.repository.PoiDAO
import com.antoniomy.data.repository.RemoteJson
import com.antoniomy.data.repository.RemoteJsonImpl
import com.antoniomy.data.repository.RemoteRetrofit
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

    //Remote Repository
    @Provides
    @Singleton
    fun remoteService(retrofit: Retrofit): RemoteRetrofit = retrofit.create(RemoteRetrofit::class.java)

    //Remote Mock Json
    @Provides
    @Singleton
    fun remoteJson() : RemoteJson = RemoteJsonImpl()

    @Provides
    @Singleton
    fun providesDistrictRemoteRepo(remoteRetrofit: RemoteRetrofit): RemoteRepository = RemoteRepositoryImpl(remoteRetrofit , remoteJson())


    //Local DB Room
    @Provides
    fun providesPoiDao(poiDB: PoiDB): PoiDAO = poiDB.getPoiDAO()

    @Provides
    @Singleton
    fun providePoiDB(@ApplicationContext context: Context) : PoiDB = Room.databaseBuilder(context, PoiDB::class.java, "PoiLocal.db").fallbackToDestructiveMigration().build()

    @Provides
    fun providePoiRepository(poiDAO: PoiDAO) : LocalRepository = LocalRepositoryImpl(poiDAO)
}