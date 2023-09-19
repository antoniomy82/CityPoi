package com.antoniomy.data.datasource

import android.content.Context
import com.antoniomy.data.model.DistrictDto

interface RemoteJson {
    fun getPoiJsonList(context: Context) : DistrictDto
}