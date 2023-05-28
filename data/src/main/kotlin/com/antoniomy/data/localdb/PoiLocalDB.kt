package com.antoniomy.data.localdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.antoniomy.domain.model.Poi

@Database(entities = [Poi::class], version = 1)

abstract class PoiLocalDB : RoomDatabase() {

    abstract fun poiLocalDAO(): PoiLocalDAO

    companion object {
        private var PoiINSTANCE: PoiLocalDB? = null

        fun getDatabaseClient(context: Context): PoiLocalDB? {


            if (PoiINSTANCE != null) return PoiINSTANCE

            synchronized(PoiLocalDB::class.java) {
                PoiINSTANCE = Room.databaseBuilder(context, PoiLocalDB::class.java, "PoiLocalDB")
                    .fallbackToDestructiveMigration().build()
                return PoiINSTANCE
            }
        }
    }
}