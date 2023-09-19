package com.antoniomy.domain.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.antoniomy.data.datasource.PoiDAO
import com.antoniomy.data.model.PoisDto

@Database(entities = [PoisDto::class], version = 1, exportSchema = false)
abstract class PoiDB : RoomDatabase() {
    abstract fun getPoiDAO(): PoiDAO
}