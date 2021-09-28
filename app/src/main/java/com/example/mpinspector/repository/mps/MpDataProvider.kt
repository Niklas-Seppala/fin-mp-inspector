package com.example.mpinspector.repository.mps

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mpinspector.repository.models.CommentModel
import com.example.mpinspector.repository.models.FavoriteModel
import com.example.mpinspector.repository.models.MpModel

interface MpDataProvider {
    suspend fun getMpImage(id: Int): Bitmap
    suspend fun getMembersOfParliament(): List<MpModel>
    suspend fun getFavoriteMembersOfParliament(): List<MpModel>
    suspend fun getFavorites(): List<FavoriteModel>
    suspend fun getMemberOfParliament(id: Int): MpModel
    suspend fun getMpComments(id: Int): MutableList<CommentModel>
    suspend fun insertMpComment(comment: CommentModel)
    suspend fun addFavMp(favorite: FavoriteModel)
    suspend fun removeFavMp(favorite: FavoriteModel)
}