package com.antoniomy.domain.datasource.remote

import com.antoniomy.domain.model.District
import kotlinx.coroutines.flow.MutableStateFlow

interface RemoteRepository {
    suspend fun getDistrictList(urlId: String) : MutableStateFlow<District>
}