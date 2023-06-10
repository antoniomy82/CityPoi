package com.antoniomy.domain.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.antoniomy.data.model.PoisDto
import com.antoniomy.data.repository.PoiDAO

@Database(entities = [PoisDto::class], version = 1, exportSchema = false)
abstract class PoiDB : RoomDatabase() {
    abstract fun getPoiDAO(): PoiDAO
}