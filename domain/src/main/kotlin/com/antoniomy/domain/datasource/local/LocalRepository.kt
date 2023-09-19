package com.antoniomy.domain.datasource.local

import com.antoniomy.domain.model.Poi
import kotlinx.coroutines.flow.MutableStateFlow

interface LocalRepository {

    fun insertPoi(poi: Poi)

    fun deletePoi(name: String)

    fun fetchPoiList(): MutableStateFlow<List<Poi>>


    suspend fun readPoi(name: String): MutableStateFlow<Poi>
}