package com.vmatdev.testprojectone.network.objects.post.data

import java.text.SimpleDateFormat
import java.util.*

class PostDto(
        val id: String,
        val title: String,
        val text: String,
        val image: String,
        val sort: Int,
        val date: String
) {
    fun getCalendarDate(): Calendar {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        return Calendar.getInstance().apply {
            time = dateFormat.parse(date)
        }
    }
}