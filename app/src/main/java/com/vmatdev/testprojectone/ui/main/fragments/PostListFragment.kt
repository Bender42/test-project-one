package com.vmatdev.testprojectone.ui.main.fragments

import androidx.lifecycle.Observer
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vmatdev.testprojectone.R
import com.vmatdev.testprojectone.network.objects.post.data.PostDto
import com.vmatdev.testprojectone.ui.main.MainActivity
import com.vmatdev.testprojectone.ui.main.adapter.PostListAdapter
import com.vmatdev.testprojectone.ui.main.model.SortType
import com.vmatdev.testprojectone.ui.main.model.SortTypeModel
import com.vmatdev.testprojectone.ui.main.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_post_list.*

class PostListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_post_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SortTypeModel(sort_type_container, getViewModel().sortType) {
            getViewModel().sortType = it
            setPostsToAdapter(getViewModel().posts.value)
        }
        swipe_refresh_post_list.setOnRefreshListener {
            getViewModel().loadPosts(false)
        }
        ContextCompat.getDrawable(view.context, R.drawable.item_divider)?.let {
            val decoration = DividerItemDecoration(view.context, LinearLayoutManager.VERTICAL)
            decoration.setDrawable(it)
            post_list.addItemDecoration(decoration)
        }
        post_list.layoutManager = LinearLayoutManager(view.context)
        post_list.adapter = PostListAdapter { post ->
            activity?.let {
                it as MainActivity
                it.onPostSelected(post.id)
            }
        }
        getViewModel().posts.observe(this, Observer<List<PostDto>> {
            setPostsToAdapter(it)
        })
        getViewModel().messages.observe(this, Observer {
            it?.let { Snackbar.make(post_list, it, Snackbar.LENGTH_LONG).show() }
        })
        getViewModel().progress.observe(this, Observer {
            swipe_refresh_post_list.isRefreshing = it == true
        })
    }

    private fun setPostsToAdapter(posts: List<PostDto>?) {
        val serverSort = posts ?: ArrayList()
        post_list.adapter?.let { adapter ->
            adapter as PostListAdapter
            val sortedPosts = if (getViewModel().sortType == SortType.SORT_TYPE_DATE) {
                serverSort.sortedBy { it.getCalendarDate() }
            } else {
                serverSort
            }
            adapter.setPosts(sortedPosts)
        }
    }

    private fun getViewModel(): MainViewModel = (activity as MainActivity).viewModel
}
