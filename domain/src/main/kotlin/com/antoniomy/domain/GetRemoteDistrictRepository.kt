package com.antoniomy.domain

import com.antoniomy.data.model.DistrictDto
import com.antoniomy.domain.model.District
import kotlinx.coroutines.flow.MutableStateFlow

interface GetRemoteDistrictRepository {
    fun retrieveDataStatus(urlID: String): MutableStateFlow<Boolean>
    fun getRemoteDistrict(urlID: String): MutableStateFlow<DistrictDto>
    fun districtRemoteToDistrictMapper(): MutableStateFlow<District>
}