package com.example.mpinspector.ui.mpinspect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MpViewModelFactory(var mpId: Int): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Int::class.java)
            .newInstance(mpId)
    }
}