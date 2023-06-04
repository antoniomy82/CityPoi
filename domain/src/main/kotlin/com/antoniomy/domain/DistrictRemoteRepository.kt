package com.antoniomy.domain

import com.antoniomy.domain.model.District
import kotlinx.coroutines.flow.MutableStateFlow

interface DistrictRemoteRepository {
    suspend fun getDistrictList(urlId: String) : MutableStateFlow<District>
}