package com.antoniomy.data

import android.util.Log
import com.antoniomy.data.service.RemoteConnection
import com.antoniomy.domain.model.DistrictRemote
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DistrictRemoteDataSource {

    fun getDistrictList(urlId: String): MutableStateFlow<DistrictRemote> =
        requestDistrictList(RemoteConnection.remoteService.getDistrictList(urlCities + urlId))

    private fun requestDistrictList(call: Call<DistrictRemote>): MutableStateFlow<DistrictRemote> {
        val retrieveDistrict = MutableStateFlow(DistrictRemote())

        call.enqueue(object : Callback<DistrictRemote> {
            override fun onResponse(call: Call<DistrictRemote>, response: Response<DistrictRemote>) {
                Log.d("_response", response.body().toString())
                response.body()?.let { retrieveDistrict.value = it }
            }

            override fun onFailure(call: Call<DistrictRemote>, t: Throwable) {
                Log.e("__error",t.toString())
            }

        })
        return retrieveDistrict
    }
}