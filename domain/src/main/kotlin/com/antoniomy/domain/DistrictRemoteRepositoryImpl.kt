package com.antoniomy.domain

import android.util.Log
import com.antoniomy.data.repository.RemoteService
import com.antoniomy.data.repository.urlCities
import com.antoniomy.domain.model.District
import com.antoniomy.domain.model.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject


class DistrictRemoteRepositoryImpl @Inject constructor(private val remoteService: RemoteService) : DistrictRemoteRepository {


    override suspend fun getDistrictList(urlId: String): MutableStateFlow<District> =
        withContext(Dispatchers.IO) {
            val retrieveDistrict = MutableStateFlow(District())

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
            return@withContext retrieveDistrict
        }


}
