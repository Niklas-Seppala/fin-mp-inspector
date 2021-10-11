package com.example.mpinspector.repository.mps

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.mpinspector.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
class InvalidCacheReadException(cacheId: String) :
    Exception("No requested \"$cacheId\" cached image exists.")

/**
 * Class for caching images to device disk. If no image is yet cached,
 * it will get fetched from network, and stored to disk.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
class ImageCache {
    private var cache = mutableMapOf<String, File>()
    private var cacheInitFlag = false

    /**
     * Loads cache from device cache dir and maps image files to keys.
     * If cache dir does not yet exist, it will get created,
     * and cache map will be empty.
     */
    fun load() {
        // Only load once.
        if (cacheInitFlag) return

        // Load cache dir / create one.
        val cacheDir = File("${App.appContext.cacheDir}/img")
        if (!cacheDir.exists())
            cacheDir.mkdir()

        // Populate cache image map.
        cacheDir.listFiles()
            ?.filter { it.extension == "jpg" }
            ?.map { it.nameWithoutExtension to it }
            ?.apply {
                cacheInitFlag = true
                cache.putAll(this)
            }
    }

    /**
     * Fetch MP image from network and stores it to device
     * cache dir.
     *
     * @param id: Int Mp id
     * @param size ImageSize Enum
     * @return Bitmap MP image as a Bitmap object.
     */
    @Throws(InvalidCacheReadException::class)
    suspend fun fetch(id: Int, size: ImageSize): Bitmap {
        // Map size to cache key.
        val cacheId = when (size) {
            ImageSize.SMALL -> "${id}_small"
            ImageSize.NORMAL -> "$id"
        }

        // Load image from disc.
        return withContext<Bitmap>(Dispatchers.IO) {
            val file = cache[cacheId] ?: throw InvalidCacheReadException(cacheId)
            BitmapFactory.decodeStream(file.inputStream())
        }
    }

    /**
     * @param key String cached image key.
     */
    fun containsKey(key: String) = cache.containsKey(key)

    /**
     * Insert file to cache by key.
     */
    fun insert(key: String, file: File) {
        cache[key] = file
    }
}