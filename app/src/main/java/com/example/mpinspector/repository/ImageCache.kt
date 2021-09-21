package com.example.mpinspector.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.Exception

class InvalidCacheReadException(msg: String) : Exception(msg)

class ImageCache() {
    private var cache = mutableMapOf<Int, File>()
    private var cacheInitFlag = false

    fun load(context: Context) {
        if (cacheInitFlag) return

        val imgCacheDir = File("${context.cacheDir}/img")
        if (!imgCacheDir.exists()) imgCacheDir.mkdir()

        val files = imgCacheDir.listFiles()
            ?.filter { it.extension == "jpg" }
            ?.map { it.nameWithoutExtension.toInt() to it }
        if (files != null) {
            cache.putAll(files)
        }
        cacheInitFlag = true
    }

    @Throws(InvalidCacheReadException::class)
    suspend fun fetch(id: Int): Bitmap {
        return withContext<Bitmap>(Dispatchers.IO) {
            val file = cache[id] ?: throw InvalidCacheReadException(
                "No requested \"$id\" cached image exists.")
            return@withContext BitmapFactory.decodeStream(file.inputStream())
        }
    }

    fun containsKey(key: Int) = cache.containsKey(key)
    fun insert(key: Int, file: File) { cache[key] = file }
}