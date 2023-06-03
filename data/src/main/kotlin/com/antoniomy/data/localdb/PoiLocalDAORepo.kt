package com.antoniomy.data.localdb

import android.content.Context
import android.util.Log
import com.antoniomy.data.model.PoisDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PoiLocalDAORepo {

    //DB instance
    private var poiLocalDB: PoiLocalDB? = null

    private fun initializeDB(context: Context): PoiLocalDB? {
        return PoiLocalDB.getDatabaseClient(context)
    }

    fun insertPoi(context: Context, poi: PoisDto): Boolean {

        var operationStatus = false

        poiLocalDB = initializeDB(context)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                poiLocalDB?.poiLocalDAO()?.insertPoi(poi)
                operationStatus = true
            } catch (e: Exception) {
                Log.e("__insertError", e.toString())
            }
        }

        return operationStatus
    }

    fun deletePoi(context: Context, name: String): Boolean {
        var operationStatus = false

        poiLocalDB = initializeDB(context)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                poiLocalDB?.poiLocalDAO()?.deletePoi(name)
                operationStatus = true
            } catch (e: Exception) {
                Log.e("__deleteError", e.toString())
            }
        }
        return operationStatus
    }

    fun fetchPois(context: Context): List<PoisDto> {

        val mList = mutableListOf<PoisDto>()

        poiLocalDB = initializeDB(context)

        CoroutineScope(Dispatchers.IO).launch {
            val mSize = poiLocalDB?.poiLocalDAO()?.fetchPois()?.size ?: 0

            for (i in 0 until mSize) {
                poiLocalDB?.poiLocalDAO()?.fetchPois()?.get(i)?.let {
                    mList.add(i, it)
                }
            }
        }

        return mList
    }
}