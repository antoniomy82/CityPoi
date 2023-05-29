package com.antoniomy.data.repository

import com.antoniomy.data.model.DistrictDto
import com.antoniomy.data.remote.ApiResource

interface DistrictRemoteRepo{
    suspend fun getDistrict(urlId: String): ApiResource<DistrictDto>
}

