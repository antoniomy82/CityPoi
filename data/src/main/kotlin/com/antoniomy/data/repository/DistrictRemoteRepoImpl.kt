package com.antoniomy.data.repository

import com.antoniomy.data.di.urlCities
import com.antoniomy.data.remote.ApiService
import com.antoniomy.data.remote.safeApiCall
import javax.inject.Inject

class DistrictRemoteRepoImpl @Inject constructor(private val apiService: ApiService) :
    DistrictRemoteRepo {

    override suspend fun getDistrict(urlId: String) = safeApiCall {
        apiService.getDistrictList(urlCities+urlId)
    }
}