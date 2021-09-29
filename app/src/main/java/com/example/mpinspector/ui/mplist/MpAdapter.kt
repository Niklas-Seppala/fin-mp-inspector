package com.example.mpinspector.ui.mplist

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.mpinspector.R
import com.example.mpinspector.databinding.FragmentItemBinding

import com.example.mpinspector.repository.models.MpModel
import com.example.mpinspector.ui.mpinspect.ViewHolder
import com.example.mpinspector.utils.PartyMapper

@SuppressLint("NotifyDataSetChanged")
class MpAdapter(items: List<MpModel>) : RecyclerView.Adapter<ViewHolder>() {
    private val mps: MutableList<MpModel> = items.toMutableList()
    private val allItems = items

    private var lightColor: Int = 0
    private var darkColor: Int = 0

    override fun getItemCount(): Int = mps.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentItemBinding.inflate(inflater, parent, false)

        lightColor = ContextCompat.getColor(binding.root.context, R.color.comment_dark)
        darkColor = ContextCompat.getColor(binding.root.context, R.color.comment_light)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding as FragmentItemBinding
        val item = mps[position]
        holder.binding.content.text = holder.itemView.context.getString(
            R.string.mpFragFullName, item.first, item.last)
        holder.binding.partyLogoIv.setImageResource(PartyMapper.partyIcon(item.party))
        holder.binding.root.setBackgroundColor(if (position % 2 == 0 ) darkColor else lightColor)

        holder.itemView.setOnClickListener {
            val nav = it.findNavController()
            when (nav.currentDestination?.id ?: -1) {
                R.id.nav_fav_mps -> navigateToFromFavorites(item.personNumber, nav)
                R.id.nav_mp_list -> navigateToFromAll(item.personNumber, nav)
            }
        }
    }

    private fun navigateToFromFavorites(arg: Int, nav: NavController) {
        val action = FavoriteMpListFragmentDirections.actionNavFavMpsToNavMpInspect()
        action.mpId = arg
        nav.navigate(action)
    }

    private fun navigateToFromAll(arg: Int, nav: NavController) {
        val action = MpListItemFragmentDirections.actionMpListItemFragmentToMpFragment()
        action.mpId = arg
        nav.navigate(action)
    }

    fun performFiltering(active: Set<String>, text: String) {
        val new = allItems.filter { it.party in active }
            .filter { "${it.first} {$it.last}".contains(text, ignoreCase = true) }
            .toList()
        mps.clear()
        mps.addAll(new)
        notifyDataSetChanged()
    }

    fun loadItems(new: List<MpModel>) {
        mps.clear()
        mps.addAll(new)
        notifyDataSetChanged()
    }
}