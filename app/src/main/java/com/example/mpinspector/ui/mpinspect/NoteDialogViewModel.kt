package com.example.mpinspector.ui.mpinspect

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel class for Note dialog view.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 */
class NoteDialogViewModel : ViewModel() {

    /**
     *
     */
    fun likeClicked() {
        likedLiveData.value?.let {
            likePrevState = it
            likedLiveData.value = !it
            dislikePrevState = dislikedLiveData.value!!
            dislikedLiveData.value = false
        }
    }

    /**
     * Stores dislike's previous state to helper var,
     * and negates dislike state.
     */
    fun dislikeClicked() {
        dislikedLiveData.value?.let {
            dislikePrevState = it
            dislikedLiveData.value = !it
            likePrevState = likedLiveData.value!!
            likedLiveData.value = false
        }
    }

    /**
     * Checks if either of the like buttons is active.
     */
    fun oneOfLikeButtonsIsActive(): Boolean {
        return (likedLiveData.value ?: false) || (dislikedLiveData.value ?: false)
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