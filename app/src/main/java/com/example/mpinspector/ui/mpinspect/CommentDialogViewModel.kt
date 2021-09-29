package com.example.mpinspector.ui.mpinspect

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CommentDialogViewModel : ViewModel() {

    fun likeClicked() {
        likedLiveData.value?.let {
            likedLiveData.value = !it
        }
    }

    fun dislikeClicked() {
        dislikedLiveData.value?.let {
            dislikedLiveData.value = !it
        }
    }

    private val likedLiveData = MutableLiveData(false)
    val isLikeActive: LiveData<Boolean>
        get() = likedLiveData


    private val dislikedLiveData = MutableLiveData(false)
    val isDislikeActive: LiveData<Boolean>
        get() = dislikedLiveData

}