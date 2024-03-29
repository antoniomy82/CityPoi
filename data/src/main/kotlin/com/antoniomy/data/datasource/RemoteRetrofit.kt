package com.antoniomy.data.datasource

import com.antoniomy.data.model.DistrictDto
import retrofit2.http.GET
import retrofit2.http.Url

interface RemoteRetrofit {
    @GET suspend fun getDistrictList(@Url mUrl: String): DistrictDto
}