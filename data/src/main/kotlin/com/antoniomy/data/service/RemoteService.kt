package com.antoniomy.data.service

import com.antoniomy.domain.model.DistrictRemote
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface RemoteService {
    @GET fun getDistrictList(@Url mUrl: String): Call<DistrictRemote>
}