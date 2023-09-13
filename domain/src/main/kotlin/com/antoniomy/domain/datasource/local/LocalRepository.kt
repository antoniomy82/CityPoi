package com.antoniomy.domain.datasource.local

import com.antoniomy.domain.model.Poi
import kotlinx.coroutines.flow.MutableStateFlow

interface LocalRepository {

    fun insertPoi(poi: Poi): Boolean

    fun deletePoi(name: String): Boolean

    fun fetchPoiList(): MutableStateFlow<List<Poi>>

    suspend fun readPoi(name: String): MutableStateFlow<Poi>
}