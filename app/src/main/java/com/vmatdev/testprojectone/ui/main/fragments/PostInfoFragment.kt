package com.vmatdev.testprojectone.ui.main.fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.request.transition.ViewPropertyTransition
import com.vmatdev.testprojectone.R
import com.vmatdev.testprojectone.ui.main.MainActivity
import com.vmatdev.testprojectone.ui.main.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_post_info.*

class PostInfoFragment : Fragment() {

    private val animationObject = ViewPropertyTransition.Animator { view ->
        view.alpha = 0f
        val fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
        fadeAnim.duration = 500
        fadeAnim.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updatePostView()
    }

    fun updatePostView() {
        val selectedPost = getViewModel().selectedPost
        if (selectedPost != null) {
            not_selected.visibility = View.GONE
            image_content.visibility = View.VISIBLE
            title.visibility = View.VISIBLE
            text.visibility = View.VISIBLE

            title.text = selectedPost.title
            text.text = selectedPost.text
            Glide.with(this)
                .load("http://dev-exam.l-tech.ru" + selectedPost.image)
                .transition(GenericTransitionOptions.with(animationObject))
                .error(R.drawable.image_placeholder)
                .centerCrop()
                .into(image)
        } else {
            not_selected.visibility = View.VISIBLE
            image_content.visibility = View.GONE
            title.visibility = View.GONE
            text.visibility = View.GONE
        }
    }

    private fun getViewModel(): MainViewModel = (activity as MainActivity).viewModel
}
