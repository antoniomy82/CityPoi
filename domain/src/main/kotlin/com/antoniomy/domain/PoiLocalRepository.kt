package com.antoniomy.domain

import android.content.Context
import com.antoniomy.domain.model.Poi

interface PoiLocalRepository {

    fun insertPoi(context: Context, poi: Poi): Boolean

    fun deletePoi(context: Context, name: String): Boolean

    fun fetchPois(context: Context): List<Poi>
}