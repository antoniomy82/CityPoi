package com.antoniomy.domain.repository.local

import android.util.Log
import com.antoniomy.data.datasource.PoiDAO
import com.antoniomy.data.model.PoisDto
import com.antoniomy.domain.model.Poi
import com.antoniomy.domain.model.toData
import com.antoniomy.domain.model.toDomain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(private val poiDAO: PoiDAO) : LocalRepository {

    override fun insertPoi(poi: Poi){
        CoroutineScope(Dispatchers.IO).launch {
         try { poiDAO.insertPoi(poi.toData())
            } catch (e: Exception) { Log.e("__insertError", e.toString()) } }
    }

    override fun deletePoi(name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try { poiDAO.deletePoi(name)
            } catch (e: Exception) { Log.e("__deleteError", e.toString()) }
        }
    }

    override fun fetchPoiList(): MutableStateFlow<List<Poi>> {
        val fetchPois = MutableStateFlow(listOf<Poi>())

        CoroutineScope(Dispatchers.IO).launch {
            val mList = mutableListOf<PoisDto>()
            for (i in 0 until poiDAO.fetchPois().size) {
                mList.add(i, poiDAO.fetchPois()[i])
                Log.d("List POI", mList.toString())
            }
            fetchPois.value = mList.toDomain()
        }

        return fetchPois
    }

    override suspend fun readPoi(name: String): MutableStateFlow<Poi> =
        withContext(Dispatchers.IO) {
            MutableStateFlow(poiDAO.readPoi(name).toDomain())
        }
}
