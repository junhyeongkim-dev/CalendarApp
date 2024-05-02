package com.android.calendarapp.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.nio.charset.Charset

object GsonUtil {

    inline fun<reified T> readJsonData (fileName: String, context: Context): List<T> {
        return try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val json = String(buffer, Charset.forName("UTF-8"))

            val gson = Gson()
            val listType = object : TypeToken<List<T>>() {}.type
            gson.fromJson(json, listType)
        }catch (e: Exception) {
            emptyList()
        }
    }
}