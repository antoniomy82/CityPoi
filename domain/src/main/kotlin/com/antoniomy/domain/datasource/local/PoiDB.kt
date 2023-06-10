package com.antoniomy.domain.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.antoniomy.data.model.PoisDto
import com.antoniomy.data.repository.local.PoiDAO

@Database(entities = [PoisDto::class], version = 1)
abstract class PoiDB : RoomDatabase() {

    abstract fun poiDAO(): PoiDAO

   companion object {
      private var PoiINSTANCE: PoiDB?=null

        fun getDatabaseClient(context: Context): PoiDB? {

            if(PoiINSTANCE !=null) return PoiINSTANCE

            synchronized(PoiDB::class.java) {
                PoiINSTANCE = Room.databaseBuilder(context, PoiDB::class.java, "PoiLocalDB").fallbackToDestructiveMigration().build()
                return PoiINSTANCE
            }
        }
    }
}