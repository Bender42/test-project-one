package com.vmatdev.testprojectone.ui.main.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.os.Handler
import com.vmatdev.testprojectone.events.SingleLiveEvents
import com.vmatdev.testprojectone.network.CallResult
import com.vmatdev.testprojectone.network.MyConnector
import com.vmatdev.testprojectone.network.objects.post.PostsResponse
import com.vmatdev.testprojectone.network.objects.post.data.PostDto
import com.vmatdev.testprojectone.ui.main.model.SortType

class MainViewModel : ViewModel() {

    var connector = MyConnector()

    val progress = SingleLiveEvents<Boolean>()
    val messages = SingleLiveEvents<String>()
    val posts: MutableLiveData<List<PostDto>> by lazy {
        MutableLiveData<List<PostDto>>().also {
            loadPosts(false)
        }
    }
    var selectedPostId: String = ""
    var sortType: SortType = SortType.SORT_TYPE_SERVER

    fun loadPosts(inBackground: Boolean) {
        if (!inBackground) {
            progress.postValue(true)
        }
        Handler().postDelayed({
            connector.getPosts {
                progress.postValue(false)
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
        }, 500)
    }
}