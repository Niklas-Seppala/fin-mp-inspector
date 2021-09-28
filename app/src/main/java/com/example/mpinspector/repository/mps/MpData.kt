package com.example.mpinspector.repository.mps

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import com.example.mpinspector.MyApp
import com.example.mpinspector.repository.Repository
import com.example.mpinspector.repository.db.MpDatabase
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
    private var mpSesCache: List<MpModel> = listOf()
    private var mpFavSesCache: MutableList<FavoriteModel> = mutableListOf()

    override suspend fun getMpImage(id: Int): Bitmap {
        imageCache.load()
        val image = if (imageCache.containsKey(id))
            imageCache.fetch(id)
        else
            networkFetch(id)
        return BitmapUtil.roundCorners(image)
    }

    override suspend fun getMembersOfParliament(): List<MpModel> {
        if (mpSesCache.isNotEmpty())
            return mpSesCache

        validateMpCache()
        return mpSesCache
    }

    override suspend fun getFavoriteMembersOfParliament(): List<MpModel> {
        validateFavCache()
        validateMpCache()
        val ord = mpFavSesCache.withIndex().associate { it.value.mpId to it.index }
        return mpSesCache.filter { m -> mpFavSesCache.any { it.mpId == m.personNumber } }
            .sortedBy { ord[it.personNumber] }
    }

    override suspend fun getFavorites(): List<FavoriteModel> {
        return MpDatabase.instance.mpDao().getAllFavorites()
    }

    override fun getMp(id: Int): LiveData<MpModel> {
        return MpDatabase.instance.mpDao().getMpById(id)
    }

    override fun getMpComments(id: Int): LiveData<MutableList<CommentModel>> {
        return MpDatabase.instance.commentDao().getAllForMp(id)
    }

    override suspend fun insertMpComment(comment: CommentModel) {
        withContext(Dispatchers.IO) {
            MpDatabase.instance.commentDao().insert(comment)
        }
    }

    override suspend fun addFavMp(favorite: FavoriteModel) {
        withContext(Dispatchers.IO) {
            MpDatabase.instance.mpDao().insertFavorite(favorite)
            if (mpFavSesCache.any { it.mpId == favorite.mpId }) {
                return@withContext
            }

            validateFavCache()
            mpFavSesCache.add(favorite)
        }
    }

    override suspend fun removeFavMp(favorite: FavoriteModel) {
        withContext(Dispatchers.IO) {
            MpDatabase.instance.mpDao().deleteFavorite(favorite)
            mpFavSesCache.removeIf { it.mpId == favorite.mpId }
        }
    }

    private suspend fun validateFavCache() {
        if (mpFavSesCache.isEmpty())
            mpFavSesCache = MpDatabase.instance.mpDao().getAllFavorites().toMutableList()
    }

    private suspend fun validateMpCache() {
        if (mpSesCache.isEmpty()) {
            mpSesCache = withContext(Dispatchers.IO) {
                val dao = MpDatabase.instance.mpDao()
                var values = dao.getAllMps()
                if (values.isEmpty()) {
                    values = mpWebService.getMps()
                    for (mp in values)
                        dao.insertOrUpdate(mp)
                }
                values
            }
        }
    }

    private suspend fun networkFetch(id: Int): Bitmap {
        val endP = MpDatabase.instance.mpDao().getMpPicById(id)
        val resp = imgWebService.getImage(endP)
        val bm = BitmapUtil.resizeBitmap(BitmapFactory.decodeStream(resp.byteStream()), 300)
        val out = File(MyApp.appContext.cacheDir, "img/$id.jpg")
        bm.compress(Bitmap.CompressFormat.JPEG, 70, out.outputStream())
        imageCache.insert(id, out)
        return bm
    }
}