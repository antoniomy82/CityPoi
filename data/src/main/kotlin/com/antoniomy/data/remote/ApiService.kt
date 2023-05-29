package com.antoniomy.data.remote

import com.antoniomy.data.model.DistrictDto
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET suspend fun getDistrictList(@Url mUrl: String): DistrictDto //TODO To Change in clean architechture
}