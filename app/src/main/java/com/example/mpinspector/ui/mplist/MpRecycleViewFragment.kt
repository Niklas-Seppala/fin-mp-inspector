package com.example.mpinspector.ui.mplist

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mpinspector.R
import com.example.mpinspector.repository.models.MpModel
import com.example.mpinspector.ui.NavActions
import com.example.mpinspector.ui.adapters.OnRecycleViewItemClick

/**
 * Base class for fragments that use RecycleView with MpModel type.
 */
abstract class MpRecycleViewFragment : Fragment(), OnRecycleViewItemClick<MpModel> {

    /**
     * Reads current location and chooses correct navigation action.
     * Destination on all is mpInspect
     * @param itemData MpModel item clicked.
     */
    override fun onItemClick(itemData: MpModel) {
        val nav = findNavController()
        when (nav.currentDestination?.id ?: -1) {
            R.id.nav_fav_mps -> {
                val action = NavActions.fromFavToInspect
                action.mpId = itemData.personNumber
                nav.navigate(action)
            }
            R.id.nav_mp_list -> {
                val action = NavActions.fromMpListToInspect
                action.mpId = itemData.personNumber
                nav.navigate(action)
            }
        }

        // Hide keyboard, if open.
        val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = activity?.currentFocus ?: return
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}