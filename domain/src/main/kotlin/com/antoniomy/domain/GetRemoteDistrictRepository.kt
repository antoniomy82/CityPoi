package com.antoniomy.domain

import com.antoniomy.domain.model.District
import com.antoniomy.domain.model.DistrictRemote
import dagger.Provides
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Singleton

interface GetRemoteDistrictRepository {

    fun getRemoteDistrict(urlID: String): MutableStateFlow<District>
}