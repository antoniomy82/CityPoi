package com.antoniomy.domain.repository.remote

import android.content.Context
import android.util.Log
import com.antoniomy.data.datasource.RemoteJson
import com.antoniomy.data.datasource.RemoteRetrofit
import com.antoniomy.data.datasource.urlCities
import com.antoniomy.domain.model.District
import com.antoniomy.domain.model.toDomain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject


class RemoteRepositoryImpl @Inject constructor(
    private val remoteRetrofit: RemoteRetrofit,
    private val remoteJson: RemoteJson
) : RemoteRepository {
    override fun getDistrictList(urlId: String): MutableStateFlow<District> {
        val retrieveDistrict = MutableStateFlow(District())
        CoroutineScope(Dispatchers.IO).launch {
            try { retrieveDistrict.value = remoteRetrofit.getDistrictList(urlCities + urlId).toDomain() }
            catch (t: Throwable) {
                when (t) {
                    is HttpException -> Log.e("httpError->", t.code().toString())
                    else -> Log.e("Unknow Error->", t.message.toString())
                }
            }
        }
        return retrieveDistrict
    }

    override fun getMockedList(context: Context): MutableStateFlow<District> {
        val retrieveDistrict = MutableStateFlow(District())
        CoroutineScope(Dispatchers.IO).launch {
            try { retrieveDistrict.value = remoteJson.getPoiJsonList(context).toDomain() }
            catch (t: Throwable){
                when(t){
                    is HttpException ->  Log.e("httpError->", t.code().toString())
                    else -> Log.e("Unknow Error->", t.message.toString())
                }
            }
        }
        return retrieveDistrict
    }


}
