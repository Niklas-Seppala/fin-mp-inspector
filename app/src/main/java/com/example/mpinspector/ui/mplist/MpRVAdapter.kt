package com.example.mpinspector.ui.mplist

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentItemBinding

import com.example.mpinspector.repository.models.MpModel
import com.example.mpinspector.utils.PartyMapper

class MpRVAdapter(private var items: List<MpModel>)
    : RecyclerView.Adapter<MpRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.contentView.text = holder.itemView.context.getString(R.string.mpFragFullName, item.first, item.last)
        holder.partyLogoIv.setImageResource(PartyMapper.partyIcon(item.party))

        holder.itemView.setOnClickListener {
            val nav = it.findNavController()
            when (nav.currentDestination?.id ?: -1) {
                R.id.nav_fav_mps -> navigateToFromFavorites(item, nav)
                R.id.nav_mp_list -> navigateToFromAll(item, nav)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setFiltered(list: List<MpModel>) {
        items = list
        notifyDataSetChanged()
    }

    private fun navigateToFromFavorites(item: MpModel, nav: NavController) {
        val action = FavoriteMpListFragmentDirections.actionNavFavMpsToNavMpInspect()
        action.mpId = item.personNumber
        nav.navigate(action)
    }

    private fun navigateToFromAll(item: MpModel, nav: NavController) {
        val action = MpListItemFragmentDirections.actionMpListItemFragmentToMpFragment()
        action.mpId = item.personNumber
        nav.navigate(action)
    }

    inner class ViewHolder(binding: FragmentItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        val contentView: TextView = binding.content
        val partyLogoIv: ImageView = binding.partyLogoIv
    }
}