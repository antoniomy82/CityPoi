package com.antoniomy.data.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.antoniomy.data.model.PoisDto
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Database(entities = [PoisDto::class], version = 1)


abstract class PoiLocalDB : RoomDatabase() {

    abstract fun poiLocalDAO(): PoiLocalDAO

    @Module
    @InstallIn(SingletonComponent::class)
    object LocalDbBuilder {
      private var PoiINSTANCE: PoiLocalDB?=null

        @Provides
        fun getDatabaseClient(context: Context): PoiLocalDB {
            PoiINSTANCE?.let {  return it }

            synchronized(PoiLocalDB::class.java) {
                PoiINSTANCE = Room.databaseBuilder(context, PoiLocalDB::class.java, "PoiLocalDB").fallbackToDestructiveMigration().build()
                return PoiINSTANCE as PoiLocalDB
            }
        }
    }
}