package com.vmatdev.testprojectone.ui.main

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vmatdev.testprojectone.R
import com.vmatdev.testprojectone.network.objects.post.data.PostDto
import com.vmatdev.testprojectone.ui.main.adapter.PostListAdapter
import kotlinx.android.synthetic.main.fragment_post_list.view.*

class PostListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_post_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ContextCompat.getDrawable(view.context, R.drawable.item_divider)?.let {
            val decoration = DividerItemDecoration(view.context, LinearLayoutManager.VERTICAL)
            decoration.setDrawable(it)
            view.post_list.addItemDecoration(decoration)
        }
        view.post_list.layoutManager = LinearLayoutManager(view.context)
        view.post_list.adapter = PostListAdapter { post ->
            activity?.let {
                it as MainActivity
                it.onPostSelected(post.id)
            }
        }
        getViewModel().posts.observe(this, Observer<List<PostDto>> {
            view.post_list.adapter?.let { adapter ->
                adapter as PostListAdapter
                adapter.setPosts(it ?: ArrayList())
            }
        })
        getViewModel().messages.observe(this, Observer<String> {
            it?.let { Snackbar.make(view, it, Snackbar.LENGTH_LONG).show() }
        })
    }

    private fun getViewModel(): MainViewModel = (activity as MainActivity).viewModel
}
