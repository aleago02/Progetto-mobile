package com.example.playersquiz.data

import android.content.Context
import java.io.File
import java.io.IOException

class MyCacheManager(private val context: Context) {

    fun saveDataToCache(data: String, cacheFileName: String) {
        context.cacheDir?.let { cacheDir ->
            val cacheFile = File(cacheDir, cacheFileName)

            try {
                cacheFile.writeText(data)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun readDataFromCache(cacheFileName: String): String {
        context.cacheDir?.let { cacheDir ->
            val cacheFile = File(cacheDir, cacheFileName)

            if (cacheFile.exists()) {
                try {
                    return cacheFile.readText()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        return "0"
    }
}