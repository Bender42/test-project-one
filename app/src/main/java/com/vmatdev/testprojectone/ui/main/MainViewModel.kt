package com.vmatdev.testprojectone.ui.main

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.vmatdev.testprojectone.network.CallResult
import com.vmatdev.testprojectone.network.MyConnector
import com.vmatdev.testprojectone.network.objects.post.PostsResponse
import com.vmatdev.testprojectone.network.objects.post.data.PostDto

class MainViewModel : ViewModel() {

    var connector = MyConnector()

    val messages = MutableLiveData<String>()
    val posts: MutableLiveData<List<PostDto>> by lazy {
        MutableLiveData<List<PostDto>>().also {
            loadPosts()
        }
    }
    var selectedPostId: String = ""

    private fun loadPosts() {
        connector.getPosts {
            when (it) {
                is CallResult.Success -> {
                    it.response as PostsResponse
                    posts.postValue(it.response.posts)
                }
                is CallResult.Error -> {
                    messages.postValue(it.error)
                }
            }
        }
    }
}