package com.antoniomy.data.remote

import com.antoniomy.domain.model.District
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET suspend fun getDistrictList(@Url mUrl: String): District //TODO To Change in clean architechture
}