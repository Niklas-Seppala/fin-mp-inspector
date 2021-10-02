package com.example.mpinspector.repository.mps

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import com.example.mpinspector.MyApp
import com.example.mpinspector.repository.db.MpDatabase
import com.example.mpinspector.repository.models.MpTwitterModel
import com.example.mpinspector.repository.models.CommentModel
import com.example.mpinspector.repository.models.FavoriteModel
import com.example.mpinspector.repository.models.MpModel
import com.example.mpinspector.repository.network.ImageWebService
import com.example.mpinspector.repository.network.MpWebService
import com.example.mpinspector.repository.network.Network
import com.example.mpinspector.utils.BitmapUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class MpData : MpDataProvider {
    private val mpWebService = Network.mpClient.create(MpWebService::class.java)
    private val imgWebService = Network.imageClient.create(ImageWebService::class.java)
    private val imageCache = ImageCache()

    override suspend fun getMpImage(id: Int): Bitmap {
        imageCache.load()
        val image = if (imageCache.containsKey(id))
            imageCache.fetch(id)
        else
            networkFetch(id)
        return BitmapUtil.roundCorners(image)
    }

    override fun getMp(id: Int): LiveData<MpModel> {
        return MpDatabase.instance.mpDao().selectById(id)
    }

    override fun getMps(): LiveData<List<MpModel>> {
        return MpDatabase.instance.mpDao().selectAll()
    }

    override fun getFavoriteMps(): LiveData<List<MpModel>> {
        return MpDatabase.instance.favoriteDao().getAllSorted()
    }

    override fun isMpInFavorites(mpId: Int): LiveData<Boolean> {
        return MpDatabase.instance.favoriteDao().existsById(mpId)
    }

    override fun getMpsFromTwitterFeed(): LiveData<List<MpTwitterModel>> {
        return MpDatabase.instance.mpTwitterDao().getMps()
    }

    override fun getMpComments(id: Int): LiveData<MutableList<CommentModel>> {
        return MpDatabase.instance.commentDao().selectForMpId(id)
    }

    override suspend fun storeMpComment(comment: CommentModel) {
        withContext(Dispatchers.IO) {
            MpDatabase.instance.commentDao().insertOrUpdate(comment)
        }
    }

    override suspend fun storeFavoriteMp(favorite: FavoriteModel) {
        withContext(Dispatchers.IO) {
            MpDatabase.instance.favoriteDao().insert(favorite)
        }
    }

    override suspend fun deleteFavoriteMp(favorite: FavoriteModel) {
        withContext(Dispatchers.IO) {
            MpDatabase.instance.favoriteDao().delete(favorite)
        }
    }

    override suspend fun loadFromWeb() {
        withContext(Dispatchers.IO) {
            MpDatabase.instance.mpDao().insertOrUpdate(mpWebService.getMps())
            MpDatabase.instance.mpTwitterDao().insertOrUpdate(mpWebService.getMpTwitterIds())
        }
    }

    private suspend fun networkFetch(id: Int): Bitmap {
        val endP = MpDatabase.instance.mpDao().selectPicture(id)
        val resp = imgWebService.getImage(endP)
        val bm = BitmapUtil.resizeBitmap(BitmapFactory.decodeStream(resp.byteStream()), 300)
        val out = File(MyApp.appContext.cacheDir, "img/$id.jpg")
        bm.compress(Bitmap.CompressFormat.JPEG, 70, out.outputStream())
        imageCache.insert(id, out)
        return bm
    }
}