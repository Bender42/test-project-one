package com.vmatdev.testprojectone.ui.main.adapter

import androidx.recyclerview.widget.DiffUtil
import com.vmatdev.testprojectone.network.objects.post.data.PostDto

class PostListDiffUtilCallback(
    private val oldPosts: List<PostDto>,
    private val newPosts: List<PostDto>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldPosts[oldItemPosition] == newPosts[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPost = oldPosts[oldItemPosition]
        val newPost = oldPosts[newItemPosition]
        return oldPost.id == newPost.id &&
                oldPost.date == newPost.date &&
                oldPost.image == newPost.image &&
                oldPost.sort == newPost.sort &&
                oldPost.text == newPost.text &&
                oldPost.title == newPost.title
    }

    override fun getOldListSize(): Int {
        return oldPosts.size
    }

    override fun getNewListSize(): Int {
        return newPosts.size
    }
}