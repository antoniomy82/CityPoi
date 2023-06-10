package com.antoniomy.domain.datasource.local

import android.util.Log
import com.antoniomy.data.model.PoisDto
import com.antoniomy.data.repository.PoiDAO
import com.antoniomy.domain.model.Poi
import com.antoniomy.domain.model.toData
import com.antoniomy.domain.model.toDomain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(private val poiDAO: PoiDAO): LocalRepository {

    override fun insertPoi(poi: Poi) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                poiDAO.insertPoi(poi.toData())
                Log.d("INSERTED-->", poi.name)
            } catch (e: Exception) {
                Log.e("__insertError", e.toString())
            }
        }
    }

    override fun deletePoi(name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                poiDAO.deletePoi(name)
            } catch (e: Exception) {
                Log.e("__deleteError", e.toString())
            }
        }
    }

    override fun fetchPoiList(): List<Poi> {
        val mList = mutableListOf<PoisDto>()
        CoroutineScope(Dispatchers.IO).launch {
            for (i in 0 until poiDAO.fetchPois().size) {
                mList.add(i, poiDAO.fetchPois()[i])
            }
        }
        return mList.toDomain()
    }
}