package com.antoniomy.data.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.antoniomy.data.model.PoisDto

@Dao
interface PoiDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPoi(poi: PoisDto)

    @Query("DELETE FROM Poi WHERE name =:name")
    fun deletePoi(name: String)

    @Query("SELECT * FROM Poi")
    fun fetchPois(): List<PoisDto>

}