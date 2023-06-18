package com.antoniomy.domain.datasource.local

import com.antoniomy.domain.model.Poi
import kotlinx.coroutines.flow.MutableStateFlow

interface LocalRepository {

    fun insertPoi(poi: Poi)

    fun deletePoi(name: String)

    suspend fun fetchPoiList(): MutableStateFlow<List<Poi>>
}