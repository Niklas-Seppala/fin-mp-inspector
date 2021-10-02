package com.example.mpinspector.ui.mpinspect

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CommentDialogViewModel : ViewModel() {

    fun likeClicked() {
        likedLiveData.value?.let {
            likePrevState = it
            likedLiveData.value = !it
            dislikePrevState = dislikedLiveData.value!!
            dislikedLiveData.value = false
        }
    }

    fun dislikeClicked() {
        dislikedLiveData.value?.let {
            dislikePrevState = it
            dislikedLiveData.value = !it
            likePrevState = likedLiveData.value!!
            likedLiveData.value = false
        }
    }


    var likePrevState = false
    private val likedLiveData = MutableLiveData(false)
    val isLikeActive: LiveData<Boolean>
        get() = likedLiveData

    var dislikePrevState = false
    private val dislikedLiveData = MutableLiveData(false)
    val isDislikeActive: LiveData<Boolean>
        get() = dislikedLiveData

}