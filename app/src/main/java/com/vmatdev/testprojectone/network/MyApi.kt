package com.vmatdev.testprojectone.network

import com.vmatdev.testprojectone.network.objects.auth.AuthRequest
import com.vmatdev.testprojectone.network.objects.auth.AuthResponse
import com.vmatdev.testprojectone.network.objects.phoneMask.PhoneMaskResponse
import com.vmatdev.testprojectone.network.objects.post.data.PostDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MyApi {

    @GET("phone_masks")
    fun getPhoneMask(): Call<PhoneMaskResponse>

    @GET("posts")
    fun getPosts(): Call<List<PostDto>>

    @POST("auth")
    fun auth(@Body request: AuthRequest): Call<AuthResponse>
}