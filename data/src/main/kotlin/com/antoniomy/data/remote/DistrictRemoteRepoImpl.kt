package com.antoniomy.data.remote

import com.antoniomy.data.di.urlCities
import javax.inject.Inject

class DistrictRemoteRepoImpl @Inject constructor(private val remoteDto: RemoteDto) : DistrictRemoteRepo {

    override suspend fun getDistrict(urlId: String) = safeApiCall {
        remoteDto.getDistrictList(urlCities +urlId)
    }
}