package com.antoniomy.data.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.antoniomy.data.model.PoisDto

@Dao
interface PoiDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPoi(poi: PoisDto)

    @Query("DELETE FROM Poi WHERE name =:arg0")
    fun deletePoi(arg0: String)

    @Query("SELECT * FROM Poi")
    fun fetchPois(): List<PoisDto>

    @Query("SELECT * FROM Poi WHERE name == :arg0")
    fun readPoi(arg0: String): PoisDto
}