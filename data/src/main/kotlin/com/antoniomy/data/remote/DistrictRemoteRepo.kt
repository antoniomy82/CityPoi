package com.antoniomy.data.remote

import com.antoniomy.data.model.DistrictDto

interface DistrictRemoteRepo{
    suspend fun getDistrict(urlId: String): RemoteStatus<DistrictDto>
}

