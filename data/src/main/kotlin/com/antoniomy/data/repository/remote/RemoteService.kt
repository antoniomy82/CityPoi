package com.antoniomy.data.repository.remote

import com.antoniomy.data.model.DistrictDto
import retrofit2.http.GET
import retrofit2.http.Url

interface RemoteService {
    @GET suspend fun getDistrictList(@Url mUrl: String): DistrictDto
}