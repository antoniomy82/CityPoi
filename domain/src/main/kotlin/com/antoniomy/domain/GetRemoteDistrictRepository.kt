package com.antoniomy.domain

import com.antoniomy.domain.model.District
import kotlinx.coroutines.flow.MutableStateFlow

interface GetRemoteDistrictRepository {
    fun getRemoteDistrict(urlID: String): MutableStateFlow<District>
}