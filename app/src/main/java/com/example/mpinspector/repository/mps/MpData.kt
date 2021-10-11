package com.example.mpinspector.repository.mps

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import com.example.mpinspector.App
import com.example.mpinspector.repository.db.MpDatabase
import com.example.mpinspector.repository.models.*
import com.example.mpinspector.repository.network.ImageWebService
import com.example.mpinspector.repository.network.MpWebService
import com.example.mpinspector.repository.network.Network
import com.example.mpinspector.utils.BitmapUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Image size enum for loading/saving images.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
enum class ImageSize {
    NORMAL,
    SMALL
}

/**
 * Provides the application MP related data either from network or local Room Database.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
class MpData : MpDataProvider {
    private val mpWebService = Network.mpClient.create(MpWebService::class.java)
    private val imgWebService = Network.imageClient.create(ImageWebService::class.java)
    private val imageCache = ImageCache()

    /**
     * Get MP's image.
     *
     * @param id: MP id
     * @param size ImageSize Enum
     * @param roundCorner Int round value.
     */
    override suspend fun getMpImage(id: Int, size: ImageSize, roundCorner: Int): Bitmap {
        imageCache.load()

        val image = if (imageCache.containsKey(id.toString()))
            imageCache.fetch(id, size)
        else
            networkFetch(id, size)

        return BitmapUtil.roundCorners(image, roundCorner)
    }

    override fun getMp(id: Int): LiveData<MpModel> {
        return MpDatabase.instance.mpDao().selectById(id)
    }

    override fun getMps(): LiveData<List<MpModel>> {
        return MpDatabase.instance.mpDao().selectAll()
    }

    /**
     * Get Favorite MPs
     * @return LiveData<List<MpModel>> favorited MPs.
     */
    override fun getFavoriteMps(): LiveData<List<MpModel>> {
        return MpDatabase.instance.favoriteDao().getAllSorted()
    }

    /**
     * Check if specified MP is in favorites.
     * @param mpId MP id.
     * @return LiveData<Boolean> True if is.
     */
    override fun isMpInFavorites(mpId: Int): LiveData<Boolean> {
        return MpDatabase.instance.favoriteDao().existsById(mpId)
    }

    /**
     * Get specified MP and his/hers comments bundled together.
     * @param id Int MP id
     * @return LiveData<MpWithComment> MP and comments.
     */
    override fun getMpWithComments(id: Int): LiveData<MpWithComments> {
        return MpDatabase.instance.mpDao().getMpWithComments(id)
    }

    /**
     * Get comments for specified MP id.
     * @param id Int MP id
     * @return LiveData<MutableList<CommentModel> MP's comments.
     */
    override fun getMpComments(id: Int): LiveData<MutableList<CommentModel>> {
        return MpDatabase.instance.commentDao().selectForMpId(id)
    }

    /**
     * Stores new comment to database.
     * @param comment CommentModel comment db model object.
     */
    override suspend fun storeMpComment(comment: CommentModel) {
        withContext(Dispatchers.IO) {
            MpDatabase.instance.commentDao().insert(comment)
        }
    }

    /**
     * Stores new favorite MP to database.
     * @param favorite FavoriteModel favorite db model object.
     */
    override suspend fun addFavoriteMp(favorite: FavoriteModel) {
        withContext(Dispatchers.IO) {
            MpDatabase.instance.favoriteDao().insert(favorite)
        }
    }

    /**
     * Deletes favorite MP from database.
     * @param favorite FavoriteModel favorite db model object.
     */
    override suspend fun deleteFavoriteMp(favorite: FavoriteModel) {
        withContext(Dispatchers.IO) {
            MpDatabase.instance.favoriteDao().delete(favorite)
        }
    }

    /**
     * Loads newest data fro MPs from network and stores it to database.
     */
    override suspend fun loadFromWeb() {
        withContext(Dispatchers.IO) {
            MpDatabase.instance.mpDao().insertOrUpdate(mpWebService.getMps())
            MpDatabase.instance.mpTwitterDao().insertOrUpdate(mpWebService.getMpTwitterIds())
        }
    }

    /**
     * Fetches MP image from network and caches it to device cache dir.
     *
     * @param id Int MP id.
     * @param size ImageSize Size enum.
     * @return Bitmap MP image in requested size as a Bitmap object.
     */
    private suspend fun networkFetch(id: Int, size: ImageSize): Bitmap {
        val requestEndPoint = MpDatabase.instance.mpDao().selectPicture(id)
        val response = imgWebService.getImage(requestEndPoint)

        // Create a bitmaps from network response (Both normal and small).
        val normalSizeBm = BitmapUtil.resizeBitmap(BitmapFactory.decodeStream(response.byteStream()), 300)
        val out = File(App.appContext.cacheDir, "img/$id.jpg")
        val outSmall = File(App.appContext.cacheDir, "img/${id}_small.jpg")
        normalSizeBm.compress(Bitmap.CompressFormat.JPEG, 70, out.outputStream())
        val smallSizeBm = BitmapUtil.resizeBitmap(normalSizeBm, 175)
        smallSizeBm.compress(Bitmap.CompressFormat.JPEG, 70, outSmall.outputStream())
        imageCache.insert("$id", out)
        imageCache.insert("${id}_small", outSmall)

        // Return requested size to caller.
        return when (size) {
            ImageSize.SMALL -> smallSizeBm
            ImageSize.NORMAL -> normalSizeBm
        }
    }
}