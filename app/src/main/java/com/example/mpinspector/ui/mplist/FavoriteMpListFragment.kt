package com.example.mpinspector.ui.mplist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentFavMpsBinding
import com.example.mpinspector.repository.Repository
import kotlinx.coroutines.launch

class FavoriteMpListViewModel: ViewModel() {
    val mps = Repository.mps.getFavoriteMps()
}

class FavoriteMpListFragment : Fragment() {
    private lateinit var adapter: MpAdapter
    private lateinit var viewModel: FavoriteMpListViewModel
    private lateinit var binding: FragmentFavMpsBinding

    override fun onCreateView(infl: LayoutInflater, cont: ViewGroup?, sInstState: Bundle?): View {
        binding = DataBindingUtil.inflate(infl, R.layout.fragment_fav_mps, cont, false)
        viewModel = ViewModelProvider(this).get(FavoriteMpListViewModel::class.java)
        adapter = MpAdapter(listOf())
        binding.list.adapter = adapter

        viewModel.mps.observe(viewLifecycleOwner, {
            adapter.loadItems(it)
        })
        return binding.root
    }
}