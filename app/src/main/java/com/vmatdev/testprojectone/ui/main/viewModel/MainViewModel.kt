package com.vmatdev.testprojectone.ui.main.viewModel

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
    val postsInBackgroundLiveData = MutableLiveData<List<PostDto>>()
    val postsLiveData: MutableLiveData<List<PostDto>> by lazy {
        MutableLiveData<List<PostDto>>().also {
            loadPosts(false)
        }
    }
    var selectedPost: PostDto? = null
    var sortType: SortType = SortType.SORT_TYPE_SERVER
    var autoRefreshEnabled = false
    var postsRequestRunning = false

    init {
        postsInBackgroundLiveData.value = emptyList()
    }

    fun loadPosts(inBackground: Boolean) {
        postsRequestRunning = true
        if (!inBackground) {
            progress.postValue(true)
        }
        Handler().postDelayed({
            connector.getPosts {
                postsRequestRunning = false
                progress.postValue(false)
                when (it) {
                    is CallResult.Success -> {
                        it.response as PostsResponse
                        if (inBackground && !autoRefreshEnabled) {
                            if (isPostsChanged(it.response.posts, postsLiveData.value)) {
                                postsInBackgroundLiveData.postValue(it.response.posts)
                            } else {
                                postsInBackgroundLiveData.postValue(emptyList())
                            }
                        } else {
                            postsLiveData.postValue(it.response.posts)
                            postsInBackgroundLiveData.postValue(emptyList())
                        }

                    }
                    is CallResult.Error -> {
                        if (!inBackground) {
                            messages.postValue(it.error)
                        }
                    }
                }
            }
        }, 500)
    }

    fun updatePostsFromBackgroundData() {
        postsLiveData.postValue(postsInBackgroundLiveData.value)
        postsInBackgroundLiveData.postValue(emptyList())
    }

    private fun isPostsChanged(newPosts: List<PostDto>?, oldPosts: List<PostDto>?): Boolean {
        val newPostsAfterSort = if (sortType == SortType.SORT_TYPE_DATE) {
            newPosts?.sortedBy { it.getCalendarDate() }
        } else {
            newPosts
        }
        return oldPosts != newPostsAfterSort
    }
}