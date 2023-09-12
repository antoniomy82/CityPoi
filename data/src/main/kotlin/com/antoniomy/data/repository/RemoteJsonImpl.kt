package com.antoniomy.data.repository

import android.content.Context
import com.antoniomy.data.R
import com.antoniomy.data.model.DistrictDto
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream

class RemoteJsonImpl : RemoteJson {

    override fun getPoiJsonList(context: Context): DistrictDto = try {

        val jsonInput: InputStream = context.applicationContext.resources.openRawResource(R.raw.poi_mock_list)
        val outputStream = ByteArrayOutputStream()
        val buf = ByteArray(1024)
        var len: Int

        while (jsonInput.read(buf).also { len = it } != -1) {
            outputStream.write(buf, 0, len)
        }
        outputStream.close()
        jsonInput.close()

        Gson().fromJson(outputStream.toString(), DistrictDto::class.java)
    } catch (e: IOException) { DistrictDto()}

}