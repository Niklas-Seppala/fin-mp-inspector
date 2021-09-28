package com.example.mpinspector.repository.mps

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mpinspector.repository.models.CommentModel
import com.example.mpinspector.repository.models.FavoriteModel
import com.example.mpinspector.repository.models.MpModel

interface MpDataProvider {
    suspend fun getMpImage(id: Int): Bitmap
    suspend fun insertMpComment(comment: CommentModel)
    suspend fun insertFavoriteMp(favorite: FavoriteModel)
    suspend fun deleteFavoriteMp(favorite: FavoriteModel)
    suspend fun loadFromWeb()
    suspend fun isMpInFavorites(mpId: Int) : Boolean
    fun getMpComments(id: Int): LiveData<MutableList<CommentModel>>
    fun getMp(id: Int): LiveData<MpModel>
    fun getMps(): LiveData<List<MpModel>>
    fun getFavoriteMps(): LiveData<List<MpModel>>
}