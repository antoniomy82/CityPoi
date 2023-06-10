package com.antoniomy.domain.datasource.local

import com.antoniomy.data.model.PoisDto
import com.antoniomy.domain.model.Poi

interface LocalRepository {

    fun insertPoi(poi: Poi)

    fun deletePoi(name: String)

    fun fetchPoiList(): List<Poi>
}