package com.example.mpinspector.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import com.example.mpinspector.MyApp
import com.example.mpinspector.repository.db.MpDatabase
import com.example.mpinspector.repository.models.CommentModel
import com.example.mpinspector.repository.models.MemberOfParliamentModel
import com.example.mpinspector.repository.network.ImageWebService
import com.example.mpinspector.repository.network.MpWebService
import com.example.mpinspector.repository.network.Network
import com.example.mpinspector.utils.BitmapUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class Repository {
    private val imageCache = ImageCache()
    private var pmSesCache: List<MemberOfParliamentModel>? = null

    private val mpWebService = Network.mpClient.create(MpWebService::class.java)
    private val imgWebService = Network.imageClient.create(ImageWebService::class.java)

    suspend fun getMpComments(id: Int): MutableLiveData<MutableList<CommentModel>> {
        val result = MutableLiveData(MpDatabase.getInstance().commentDao().getAllForMp(id))

        result.observeForever {
            if (it.isEmpty()) return@observeForever
            GlobalScope.launch(Dispatchers.IO) {
                MpDatabase.getInstance().commentDao().insertOfUpdate(it[it.size-1])
            }
        }

        return result
    }

    suspend fun getMembersOfParliament(): List<MemberOfParliamentModel> {
        if (pmSesCache != null)
            return pmSesCache!! // TODO FIX THIS

        pmSesCache = withContext(Dispatchers.IO) {
            val dao = MpDatabase.getInstance().mpDao()
            var values = dao.getAll()
            if (values.isEmpty()) {
                values = mpWebService.getMps()
                for (mp in values)
                    dao.insertOrUpdate(mp)
            }
            values
        }
        return pmSesCache!! // TODO FIX THIS
    }

    suspend fun getImage(id: Int): Bitmap {
        imageCache.load()
        val image = if (imageCache.containsKey(id))
            imageCache.fetch(id)
        else
            networkFetch(id)
        return BitmapUtil.roundCorners(image)
    }

    private suspend fun networkFetch(id: Int, endpoint: String): Bitmap {
        val resp = imgWebService.getImage(endpoint)
        val bm = BitmapUtil.resizeBitmap(BitmapFactory.decodeStream(resp.byteStream()), 300)
        val out = File(MyApp.appContext.cacheDir, "img/$id.jpg")
        bm.compress(Bitmap.CompressFormat.JPEG, 70, out.outputStream())
        imageCache.insert(id, out)
        return bm
    }

    private suspend fun networkFetch(id: Int): Bitmap {
        val endP = MpDatabase.getInstance().mpDao().queryPicEndpoint(id)
        val resp = imgWebService.getImage(endP)
        val bm = BitmapUtil.resizeBitmap(BitmapFactory.decodeStream(resp.byteStream()), 300)
        val out = File(MyApp.appContext.cacheDir, "img/$id.jpg")
        bm.compress(Bitmap.CompressFormat.JPEG, 70, out.outputStream())
        imageCache.insert(id, out)
        return bm
    }

    companion object Instance {
        val instance = Repository()
    }
}