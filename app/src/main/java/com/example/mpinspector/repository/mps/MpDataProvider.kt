package com.example.mpinspector.repository.mps

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.example.mpinspector.repository.models.CommentModel
import com.example.mpinspector.repository.models.MemberOfParliamentModel

interface MpDataProvider {
    suspend fun getMpImage(id: Int): Bitmap
    suspend fun getMembersOfParliament(): List<MemberOfParliamentModel>
    suspend fun getMpComments(id: Int): MutableLiveData<MutableList<CommentModel>>
    suspend fun insertMpComment(comment: CommentModel)
}