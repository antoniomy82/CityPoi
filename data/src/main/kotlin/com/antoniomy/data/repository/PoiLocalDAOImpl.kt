package com.antoniomy.data.repository

import android.util.Log
import com.antoniomy.data.model.PoisDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PoiLocalDAOImpl (private val poiLocalDB: PoiLocalDB) : PoiLocalDAO {

    private var operationStatus = false

    override fun insertPoi(poi: PoisDto): Boolean {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                poiLocalDB.poiLocalDAO().insertPoi(poi)
                operationStatus = true
            } catch (e: Exception) {
                Log.e("__insertError", e.toString())
            }
        }
        return operationStatus
    }

    override fun deletePoi(name: String): Boolean {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                poiLocalDB.poiLocalDAO().deletePoi(name)
                operationStatus = true
            } catch (e: Exception) {
                Log.e("__deleteError", e.toString())
            }
        }
        return operationStatus
    }

    override fun fetchPois(): List<PoisDto> {
        val mList = mutableListOf<PoisDto>()
        CoroutineScope(Dispatchers.IO).launch {
            for (i in 0 until poiLocalDB.poiLocalDAO().fetchPois().size) {
                mList.add(i, poiLocalDB.poiLocalDAO().fetchPois()[i])
            }
        }
        return mList
    }
}
