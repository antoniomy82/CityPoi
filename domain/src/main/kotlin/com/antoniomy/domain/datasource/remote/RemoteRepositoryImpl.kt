package com.antoniomy.domain.datasource.remote

import android.content.Context
import android.util.Log
import com.antoniomy.data.repository.RemoteJson
import com.antoniomy.data.repository.RemoteService
import com.antoniomy.data.repository.urlCities
import com.antoniomy.domain.model.District
import com.antoniomy.domain.model.toDomain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject


class RemoteRepositoryImpl @Inject constructor(
    private val remoteService: RemoteService,
    private val remoteJson: RemoteJson
) : RemoteRepository {
    override fun getDistrictList(urlId: String): MutableStateFlow<District> {
        val retrieveDistrict = MutableStateFlow(District())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                retrieveDistrict.value = remoteService.getDistrictList(urlCities + urlId).toDomain()
                Log.d("Response OK->", retrieveDistrict.value.toString())
            } catch (t: Throwable) {
                when (t) {
                    is HttpException -> {
                        Log.e("httpError->", t.code().toString())
                        //ApiResource.Error(false, throwable.code(), throwable.message)
                    }

                    else -> {
                        Log.e("Unknow Error->", t.message.toString())
                        //  ApiResource.Error(true, null, throwable.message)
                    }
                }
            }
        }
        return retrieveDistrict
    }

    override fun getMockedList(context: Context): MutableStateFlow<District> {
        val retrieveDistrict = MutableStateFlow(District())
        CoroutineScope(Dispatchers.IO).launch {
            try {
                retrieveDistrict.value = remoteJson.getPoiJsonList(context).value.toDomain()
            } catch (t: Throwable){
                when(t){
                    is HttpException ->  Log.e("httpError->", t.code().toString())
                    else -> Log.e("Unknow Error->", t.message.toString())
                }
            }
        }
        return retrieveDistrict
    }


}
