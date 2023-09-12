package com.antoniomy.domain.datasource.remote

import android.content.Context
import com.antoniomy.domain.model.District
import kotlinx.coroutines.flow.MutableStateFlow

interface RemoteRepository {
    fun getDistrictList(urlId: String) : MutableStateFlow<District>
    fun getMockedList(context: Context): MutableStateFlow<District>
}