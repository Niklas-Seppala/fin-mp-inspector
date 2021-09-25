package com.example.mpinspector.repository.mps

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import com.example.mpinspector.MyApp
import com.example.mpinspector.repository.ImageCache
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

class MpData : MpDataProvider {
    private val mpWebService = Network.mpClient.create(MpWebService::class.java)
    private val imgWebService = Network.imageClient.create(ImageWebService::class.java)

    private val imageCache = ImageCache()
    private var pmSesCache: List<MemberOfParliamentModel> = listOf()

    override suspend fun getMpImage(id: Int): Bitmap {
        imageCache.load()
        val image = if (imageCache.containsKey(id))
            imageCache.fetch(id)
        else
            networkFetch(id)
        return BitmapUtil.roundCorners(image)
    }

    override suspend fun getMembersOfParliament(): List<MemberOfParliamentModel> {
        if (pmSesCache.isNotEmpty())
            return pmSesCache

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
        return pmSesCache
    }

    override suspend fun getMpComments(id: Int): MutableLiveData<MutableList<CommentModel>> {
        return MutableLiveData(MpDatabase.getInstance().commentDao().getAllForMp(id))
    }

    override suspend fun insertMpComment(comment: CommentModel) {
        withContext(Dispatchers.IO) {
            MpDatabase.getInstance().commentDao().insertOfUpdate(comment)
        }
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
}