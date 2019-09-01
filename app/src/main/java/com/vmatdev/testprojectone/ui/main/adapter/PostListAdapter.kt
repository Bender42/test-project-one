package com.vmatdev.testprojectone.ui.main.adapter

import android.animation.ObjectAnimator
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.request.transition.ViewPropertyTransition
import com.vmatdev.testprojectone.R
import com.vmatdev.testprojectone.network.objects.post.data.PostDto
import kotlinx.android.synthetic.main.view_post_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PostListAdapter(private val callback: (PostDto) -> Unit) :
    RecyclerView.Adapter<PostListAdapter.ViewHolder>() {

    private val posts = ArrayList<PostDto>()
    private val animationObject = ViewPropertyTransition.Animator { view ->
        view.alpha = 0f
        val fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
        fadeAnim.duration = 500
        fadeAnim.start()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.view_post_item,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.itemView.title.text = post.title
        holder.itemView.text.text = post.text
        holder.itemView.date.text = SimpleDateFormat(
            "dd MM yyyy hh:mm:ss",
            Locale.ENGLISH
        ).format(Date(post.getCalendarDate().timeInMillis))
        holder.itemView.setOnClickListener {
            callback.invoke(post)
        }
        Glide.with(holder.itemView.context)
            .load("http://dev-exam.l-tech.ru" + post.image)
            .transition(GenericTransitionOptions.with(animationObject))
            .error(R.drawable.image_placeholder)
            .centerCrop()
            .into(holder.itemView.image)
    }

    fun setPosts(posts: List<PostDto>) {
        val diffUtilCallback = PostListDiffUtilCallback(this.posts, posts)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback)
        this.posts.clear()
        this.posts.addAll(posts)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}