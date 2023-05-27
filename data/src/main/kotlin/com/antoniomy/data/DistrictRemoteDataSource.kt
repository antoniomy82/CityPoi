package com.antoniomy.data

import android.util.Log
import com.antoniomy.data.service.RemoteConnection
import com.antoniomy.data.model.DistrictDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DistrictRemoteDataSource {
    var retrieveDistrictDto = MutableStateFlow(DistrictDto())
    fun getDistrictList(urlId: String): MutableStateFlow<DistrictDto> = requestDistrictList(RemoteConnection.remoteService.getDistrictList(urlCities + urlId))

    private fun requestDistrictList(call: Call<DistrictDto>): MutableStateFlow<DistrictDto> {


        CoroutineScope(Dispatchers.IO).launch {
            call.enqueue(object : Callback<DistrictDto> {
                override fun onResponse(
                    call: Call<DistrictDto>,
                    response: Response<DistrictDto>
                ) {
                    Log.d("_response", response.body().toString())
                    response.body()?.let { retrieveDistrictDto.value = it }
                }

                override fun onFailure(call: Call<DistrictDto>, t: Throwable) {
                    Log.e("__error", t.toString())
                }

            })
        }
        return retrieveDistrictDto
    }
}