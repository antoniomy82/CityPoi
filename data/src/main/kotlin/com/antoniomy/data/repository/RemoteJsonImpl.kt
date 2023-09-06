package com.antoniomy.data.repository

import android.content.Context
import com.antoniomy.data.R
import com.antoniomy.data.model.DistrictDto
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class RemoteJsonImpl : RemoteJson {

    override suspend fun getPoiJsonList(context: Context): MutableStateFlow<DistrictDto> =

        withContext(Dispatchers.IO) {
            val retrieveDistrict = MutableStateFlow(DistrictDto())
            val jsonInput: InputStream = context.resources.openRawResource(R.raw.poi_mock_list)
            val outputStream = ByteArrayOutputStream()
            val buf = ByteArray(1024)
            var len: Int
            try {
                while (jsonInput.read(buf).also { len = it } != -1) {
                    outputStream.write(buf, 0, len)
                }
                outputStream.close()
                jsonInput.close()
            } catch (_: IOException) {
            }
            retrieveDistrict.value =
                Gson().fromJson(outputStream.toString(), DistrictDto::class.java)
            return@withContext retrieveDistrict
        }
}