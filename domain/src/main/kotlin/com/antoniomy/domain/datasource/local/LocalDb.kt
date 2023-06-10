package com.antoniomy.domain.datasource.local

import android.content.Context
import android.util.Log
import com.antoniomy.data.model.PoisDto
import com.antoniomy.domain.model.Poi
import com.antoniomy.domain.model.toData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocalDb {

   lateinit var poiDB : PoiDB

    private fun initializeDB(context: Context) : PoiDB = PoiDB.getDatabaseClient(context)!!

    fun insertPoi(context: Context, poi: Poi){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                poiDB = initializeDB(context)
                poiDB.poiDAO().insertPoi(poi.toData())
                Log.d("INSERTED-->", poi.name )
            } catch (e: Exception) { Log.e("__insertError", e.toString()) }
        }
    }

    fun deletePoi(context: Context, name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                poiDB = initializeDB(context)
                poiDB.poiDAO().deletePoi(name)
            } catch (e: Exception) { Log.e("__deleteError", e.toString()) }
        }
    }

    fun fetchPoiList(context: Context): List<PoisDto> {
        poiDB = initializeDB(context)
          val mList = mutableListOf<PoisDto>()
        CoroutineScope(Dispatchers.IO).launch {
            for (i in 0 until poiDB.poiDAO().fetchPois().size) {
                mList.add(i, poiDB.poiDAO().fetchPois()[i])
            }
        }
        return mList
    }
}
