package com.example.mpinspector.repository.mps

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.mpinspector.MyApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.Exception

class InvalidCacheReadException(msg: String) : Exception(msg)

class ImageCache() {
    private var cache = mutableMapOf<String, File>()
    private var cacheInitFlag = false

    fun load() {
        if (cacheInitFlag) return

        val imgCacheDir = File("${MyApp.appContext.cacheDir}/img")
        if (!imgCacheDir.exists()) imgCacheDir.mkdir()

        val files = imgCacheDir.listFiles()
            ?.filter { it.extension == "jpg" }
            ?.map { it.nameWithoutExtension to it }
        if (files != null) {
            cache.putAll(files)
        }
        cacheInitFlag = true
    }

    @Throws(InvalidCacheReadException::class)
    suspend fun fetch(id: Int, size: ImageSize): Bitmap {

        val cacheId = when (size) {
            ImageSize.SMALL -> "${id}_small"
            ImageSize.NORMAL -> "$id"
        }

        return withContext<Bitmap>(Dispatchers.IO) {
            val file = cache[cacheId] ?: throw InvalidCacheReadException(
                "No requested \"$cacheId\" cached image exists.")
            BitmapFactory.decodeStream(file.inputStream())
        }
    }

    fun containsKey(key: String) = cache.containsKey(key)

    fun insert(key: String, file: File) {
        cache[key] = file
    }
}