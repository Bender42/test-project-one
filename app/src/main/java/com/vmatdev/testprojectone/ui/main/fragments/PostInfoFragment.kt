package com.vmatdev.testprojectone.ui.main.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.vmatdev.testprojectone.R
import com.vmatdev.testprojectone.ui.main.MainActivity
import com.vmatdev.testprojectone.ui.main.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_post_info.*

class PostInfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_post_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updatePostView()
    }

    fun updatePostView() {
        val selectedPost = getViewModel().posts.value?.find { it.id == getViewModel().selectedPostId }
        if (selectedPost != null) {
            not_selected.visibility = View.GONE
            image.visibility = View.VISIBLE
            title.visibility = View.VISIBLE
            text.visibility = View.VISIBLE

            title.text = selectedPost.title
            text.text = selectedPost.text
            Glide.with(this)
                    .load("http://dev-exam.l-tech.ru" + selectedPost.image)
                    .centerCrop()
                    .into(image)
        } else {
            not_selected.visibility = View.VISIBLE
            image.visibility = View.GONE
            title.visibility = View.GONE
            text.visibility = View.GONE
        }
    }

    private fun getViewModel(): MainViewModel = (activity as MainActivity).viewModel
}
