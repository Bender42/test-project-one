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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PostDto

        if (id != other.id) return false
        if (title != other.title) return false
        if (text != other.text) return false
        if (image != other.image) return false
        if (sort != other.sort) return false
        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + image.hashCode()
        result = 31 * result + sort
        result = 31 * result + date.hashCode()
        return result
    }
}