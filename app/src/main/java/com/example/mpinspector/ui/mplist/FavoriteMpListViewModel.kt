package com.example.mpinspector.ui.mplist

import androidx.lifecycle.ViewModel
import com.example.mpinspector.repository.Repository

class FavoriteMpListViewModel: ViewModel() {
    val mps = Repository.mps.getFavoriteMps()
}