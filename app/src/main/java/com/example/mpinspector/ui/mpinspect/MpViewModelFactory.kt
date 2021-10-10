package com.example.mpinspector.ui.mpinspect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Factory that creates MpViewModel object instance with specified mpId.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 *
 * @property mpId Int Id of the MP view model models.
 */
class MpViewModelFactory(var mpId: Int): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Int::class.java)
            .newInstance(mpId)
    }
}