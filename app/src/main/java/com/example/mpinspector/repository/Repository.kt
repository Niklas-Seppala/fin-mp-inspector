package com.example.mpinspector.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mpinspector.ParliamentMembersData
import com.example.mpinspector.repository.db.MpDatabase
import com.example.mpinspector.repository.models.MemberOfParliamentModel
import com.example.mpinspector.repository.network.ImageWebService
import com.example.mpinspector.repository.network.MpWebService
import com.example.mpinspector.repository.network.Network
import com.example.mpinspector.utils.BitmapUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.RuntimeException

class Repository {
    private val imageCache = ImageCache()
    private val mpWebService = Network.mpClient.create(MpWebService::class.java)
    private val imgWebService = Network.imageClient.create(ImageWebService::class.java)

    suspend fun getMps(): LiveData<List<MemberOfParliamentModel>> {

        // PASKAA
//        val dao = MpDatabase.getInstance().mpDao()
//        val fromDb = dao.getAll()
//        Log.d("TAG", fromDb.value!!.size.toString())
//
//        if ((fromDb.value?.size ?: 0) == 0) {
//            val fromNetwork = mpWebService.getMps()
//            for (mp in fromNetwork) {
//                dao.insertOrUpdate(mp)
//            }
//            return MutableLiveData(fromNetwork)
//        }
//        return fromDb
        TODO()
    }

    @Throws(IllegalArgumentException::class)
    suspend fun getImage(context: Context?, id: Int): Bitmap { // Quality of life nullability
        context ?: throw IllegalArgumentException("Context can not be null")

        imageCache.load(context)

        val image = if (imageCache.containsKey(id))
            imageCache.fetch(id)
        else
            networkFetch(context, id)

        return BitmapUtil.roundCorners(image)
    }

    private suspend fun networkFetch(context: Context, id: Int): Bitmap {
        val picEndPoint = ParliamentMembersData.members.find { it.personNumber == id }?.picture
            ?: throw RuntimeException("No mp with specified id: \"$id\" exists.")

        val resp = imgWebService.getImage(picEndPoint)
        val bm = BitmapUtil.resizeBitmap(BitmapFactory.decodeStream(resp.byteStream()), 500)
        withContext(Dispatchers.IO) {
            val out = File(context.cacheDir, "img/$id.jpg")
            bm.compress(Bitmap.CompressFormat.JPEG, 70, out.outputStream())
            imageCache.insert(id, out)
        }
        return bm
    }

    companion object Instance {
        var instance: Repository
            private set
        init {
            instance = Repository()
        }
    }
}