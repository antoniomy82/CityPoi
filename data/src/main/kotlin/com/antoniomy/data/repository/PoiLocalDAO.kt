package com.antoniomy.data.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.antoniomy.data.model.PoisDto

@Dao
interface PoiLocalDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPoi(poi: PoisDto): Boolean

    @Query("DELETE FROM Poi WHERE name =:name")
    fun deletePoi(name: String): Boolean

    @Query("SELECT * FROM Poi")
    fun fetchPois(): List<PoisDto>

}