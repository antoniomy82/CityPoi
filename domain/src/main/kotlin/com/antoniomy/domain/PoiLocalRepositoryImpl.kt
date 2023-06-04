package com.antoniomy.domain

import android.content.Context
import com.antoniomy.data.repository.PoiLocalDAO
import com.antoniomy.data.repository.PoiLocalDAOImpl
import com.antoniomy.data.repository.PoiLocalDB
import com.antoniomy.domain.model.Poi
import com.antoniomy.domain.model.toData
import com.antoniomy.domain.model.toDomain

class PoiLocalRepositoryImpl : PoiLocalRepository {

    lateinit var localDb: PoiLocalDAO

    override fun insertPoi(context: Context, poi: Poi): Boolean {
        localDb = PoiLocalDAOImpl(PoiLocalDB.LocalDbBuilder.getDatabaseClient(context))
        return localDb.insertPoi(poi.toData())
    }

    override fun deletePoi(context: Context, name: String): Boolean {
        localDb = PoiLocalDAOImpl(PoiLocalDB.LocalDbBuilder.getDatabaseClient(context))
        return localDb.deletePoi(name)
    }

    override fun fetchPois(context: Context): List<Poi> {
        localDb = PoiLocalDAOImpl(PoiLocalDB.LocalDbBuilder.getDatabaseClient(context))
        return localDb.fetchPois().toDomain()
    }
}