package com.antoniomy.data.repository

import android.content.Context
import com.antoniomy.data.model.DistrictDto
import kotlinx.coroutines.flow.MutableStateFlow

interface RemoteJson {
    fun getPoiJsonList(context: Context) : MutableStateFlow<DistrictDto>
}