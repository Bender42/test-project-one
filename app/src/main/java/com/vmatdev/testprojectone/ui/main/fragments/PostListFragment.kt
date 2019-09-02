package com.vmatdev.testprojectone.ui.main.fragments

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.vmatdev.testprojectone.R
import com.vmatdev.testprojectone.network.objects.post.data.PostDto
import com.vmatdev.testprojectone.ui.main.MainActivity
import com.vmatdev.testprojectone.ui.main.adapter.PostListAdapter
import com.vmatdev.testprojectone.ui.main.model.SortType
import com.vmatdev.testprojectone.ui.main.model.SortTypeModel
import com.vmatdev.testprojectone.ui.main.viewModel.MainViewModel
import kotlinx.android.synthetic.main.fragment_post_list.*

class PostListFragment : Fragment() {

    private val backgroundPostsRequestHandler = Handler()
    private val backgroundPostsRequestRunnable = object : Runnable {
        override fun run() {
            if (!getViewModel().postsRequestRunning) {
                getViewModel().loadPosts(true)
            }
            backgroundPostsRequestHandler.postDelayed(this, 5000)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auto_refresh_enabled.isChecked = getViewModel().autoRefreshEnabled
        auto_refresh_enabled.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            getViewModel().autoRefreshEnabled = isChecked
        }
        SortTypeModel(sort_type_container, getViewModel().sortType) {
            getViewModel().sortType = it
            setPostsToAdapter(getViewModel().postsLiveData.value)
        }
        swipe_refresh_post_list.setOnRefreshListener {
            getViewModel().loadPosts(false)
        }
        new_data_available_mark.setOnClickListener {
            getViewModel().updatePostsFromBackgroundData()
        }
        ContextCompat.getDrawable(view.context, R.drawable.item_divider)?.let {
            val decoration = DividerItemDecoration(view.context, LinearLayoutManager.VERTICAL)
            decoration.setDrawable(it)
            post_list.addItemDecoration(decoration)
        }
        post_list.layoutManager = LinearLayoutManager(view.context)
        post_list.adapter = PostListAdapter { post ->
            activity?.let {
                getViewModel().selectedPost = post
                it as MainActivity
                it.onPostSelected()
            }
        }
        initObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        removeObservers()
    }

    override fun onStart() {
        super.onStart()
        backgroundPostsRequestHandler.postDelayed(backgroundPostsRequestRunnable, 5000)
    }

    override fun onStop() {
        super.onStop()
        backgroundPostsRequestHandler.removeCallbacks(backgroundPostsRequestRunnable)
    }

    private fun initObservers() {
        getViewModel().postsLiveData.observe(this, Observer {
            setPostsToAdapter(it)
        })
        getViewModel().postsInBackgroundLiveData.observe(this, Observer {
            if (it.isNotEmpty()) {
                new_data_available_mark.visibility = View.VISIBLE
                getPulseAnimation(new_data_available_mark).start()
            } else {
                new_data_available_mark.visibility = View.GONE
            }
        })
        getViewModel().messages.observe(this, Observer {
            it?.let { Snackbar.make(post_list, it, Snackbar.LENGTH_LONG).show() }
        })
        getViewModel().progress.observe(this, Observer {
            swipe_refresh_post_list.isRefreshing = it == true
        })
    }

    private fun removeObservers() {
        getViewModel().postsLiveData.removeObservers(this)
        getViewModel().postsInBackgroundLiveData.removeObservers(this)
        getViewModel().messages.removeObservers(this)
        getViewModel().progress.removeObservers(this)
    }

    private fun getPulseAnimation(target: View): ObjectAnimator {
        return ObjectAnimator.ofPropertyValuesHolder(
            target,
            PropertyValuesHolder.ofFloat("scaleX", 1.05f),
            PropertyValuesHolder.ofFloat("scaleY", 1.05f)
        ).apply {
            duration = 300
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
        }
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
