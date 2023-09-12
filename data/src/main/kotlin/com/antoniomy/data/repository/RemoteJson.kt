package com.antoniomy.data.repository

import android.content.Context
import com.antoniomy.data.model.DistrictDto

interface RemoteJson {
    fun getPoiJsonList(context: Context) : DistrictDto
}