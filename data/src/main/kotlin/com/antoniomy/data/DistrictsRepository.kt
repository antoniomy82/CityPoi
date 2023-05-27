package com.antoniomy.data

import com.antoniomy.data.model.DistrictDto
import kotlinx.coroutines.flow.MutableStateFlow
class DistrictsRepository(urlId: String) {
    val remoteDistrictRepositoryRemote: MutableStateFlow<DistrictDto> = DistrictRemoteDataSource().getDistrictList(urlId)
}