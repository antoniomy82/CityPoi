package com.antoniomy.data.repository

import com.antoniomy.data.remote.ApiResource
import com.antoniomy.domain.model.District

interface DistrictRemoteRepo{
    suspend fun getDistrict(urlId: String): ApiResource<District>
}

