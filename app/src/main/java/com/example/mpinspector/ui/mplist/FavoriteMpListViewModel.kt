package com.example.mpinspector.ui.mplist

import androidx.lifecycle.ViewModel
import com.example.mpinspector.repository.Repository

/**
 * ViewModel class for favorite Mps view.
 *
 * @author Niklas Seppälä - 2013018
 * @date 10/10/2021
 *
 * @property mps LiveData<List<MpModel>> RecycleView dataset.
 */
class FavoriteMpListViewModel: ViewModel() {
    val mps = Repository.mps.getFavoriteMps()
}