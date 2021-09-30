package com.example.mpinspector.ui.mpinspect

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.mpinspector.R
import com.example.mpinspector.databinding.CommentDialogBinding
import com.example.mpinspector.ui.anim.AppAnimations

class CommentDialogFragment: DialogFragment() {
    private lateinit var dialogBinding: CommentDialogBinding
    private lateinit var dialogViewModel: CommentDialogViewModel
    private var onSubmitCb: ((s: String) -> Unit)? = null

    override fun onCreateView(infl: LayoutInflater, cont: ViewGroup?, sInstState: Bundle?): View {
        dialogBinding = DataBindingUtil.inflate(infl, R.layout.comment_dialog, cont, false)
        dialogViewModel = ViewModelProvider(this).get(CommentDialogViewModel::class.java)


        dialogViewModel.isDislikeActive.observe(viewLifecycleOwner, {
            if (it) dialogBinding.dislikeButton.startAnimation(AppAnimations.iconClickGrow)
            else    dialogBinding.dislikeButton.startAnimation(AppAnimations.iconClickToNormal)
        })

        dialogViewModel.isLikeActive.observe(viewLifecycleOwner, {
            if (it) dialogBinding.likeButton.startAnimation(AppAnimations.iconClickGrow)
            else    dialogBinding.likeButton.startAnimation(AppAnimations.iconClickToNormal)
        })

        
        dialogBinding.likeButton.setOnClickListener { dialogViewModel.likeClicked() }
        dialogBinding.dislikeButton.setOnClickListener { dialogViewModel.dislikeClicked() }
        dialogBinding.cancelButton.setOnClickListener { dialog?.cancel() }
        dialogBinding.submitButton.setOnClickListener {
            onSubmitCb?.invoke(dialogBinding.commentEt.text.toString())
            dialogBinding.commentEt.text.clear()
            dismiss()
        }

        return dialogBinding.root
    }

    fun setOnSubmit(cb: (s: String) -> Unit) { onSubmitCb = cb }
}