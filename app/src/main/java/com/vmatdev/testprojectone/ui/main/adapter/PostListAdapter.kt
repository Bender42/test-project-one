package com.vmatdev.testprojectone.ui.main.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.vmatdev.testprojectone.R
import com.vmatdev.testprojectone.network.objects.post.data.PostDto
import kotlinx.android.synthetic.main.view_post_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PostListAdapter(val callback: (PostDto) -> Unit) : RecyclerView.Adapter<PostListAdapter.ViewHolder>() {

    private val posts = ArrayList<PostDto>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_post_item, parent, false))
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.itemView.title.text = post.title
        holder.itemView.text.text = post.text
        holder.itemView.date.text = SimpleDateFormat("dd MM yyyy hh:mm:ss", Locale.ENGLISH).format(Date(post.getCalendarDate().timeInMillis))
        holder.itemView.setOnClickListener {
            callback.invoke(post)
        }
        Glide.with(holder.itemView.context)
                .load("http://dev-exam.l-tech.ru" + post.image)
                .centerCrop()
                .into(holder.itemView.image)
    }

    fun setPosts(posts: List<PostDto>) {
        this.posts.clear()
        this.posts.addAll(posts)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}