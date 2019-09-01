package com.vmatdev.testprojectone.network

import com.google.gson.GsonBuilder
import com.vmatdev.testprojectone.network.objects.auth.AuthRequest
import com.vmatdev.testprojectone.network.objects.auth.AuthResponse
import com.vmatdev.testprojectone.network.objects.phoneMask.PhoneMaskResponse
import com.vmatdev.testprojectone.network.objects.post.PostsResponse
import com.vmatdev.testprojectone.network.objects.post.data.PostDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.UnknownHostException

class MyConnector {

    private var myApi: MyApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
        myApi = retrofit.create(MyApi::class.java)
    }

    fun getPhoneMask(callback: (callResult: CallResult) -> Unit) {
        val call = myApi.getPhoneMask()
        call.enqueue(object : Callback<PhoneMaskResponse> {
            override fun onResponse(call: Call<PhoneMaskResponse>, response: Response<PhoneMaskResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        callback.invoke(CallResult.Success(body))
                    } else {
                        callback.invoke(CallResult.Error("Получен пустой ответ"))
                    }
                } else {
                    callback.invoke(CallResult.Error("Плохой ответ от сервера"))
                }
            }

            override fun onFailure(call: Call<PhoneMaskResponse>, t: Throwable) {
                if (t is UnknownHostException) {
                    callback.invoke(CallResult.Error("Проверьте интернет соединение"))
                } else {
                    callback.invoke(CallResult.Error(t.localizedMessage))
                }
            }
        })
    }

    fun getPosts(callback: (callResult: CallResult) -> Unit) {
        val call = myApi.getPosts()
        call.enqueue(object : Callback<List<PostDto>> {
            override fun onResponse(call: Call<List<PostDto>>, response: Response<List<PostDto>>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        callback.invoke(CallResult.Success(PostsResponse(body)))
                    } else {
                        callback.invoke(CallResult.Error("Получен пустой ответ"))
                    }
                } else {
                    callback.invoke(CallResult.Error("Плохой ответ от сервера"))
                }
            }

            override fun onFailure(call: Call<List<PostDto>>, t: Throwable) {
                if (t is UnknownHostException) {
                    callback.invoke(CallResult.Error("Проверьте интернет соединение"))
                } else {
                    callback.invoke(CallResult.Error(t.localizedMessage))
                }
            }
        })
    }

    fun auth(request: AuthRequest, callback: (callResult: CallResult) -> Unit) {
        val call = myApi.auth(request)
        call.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        callback.invoke(CallResult.Success(body))
                    } else {
                        callback.invoke(CallResult.Error("Получен пустой ответ"))
                    }
                } else {
                    callback.invoke(CallResult.Error("Неверный логин или пароль"))
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                if (t is UnknownHostException) {
                    callback.invoke(CallResult.Error("Проверьте интернет соединение"))
                } else {
                    callback.invoke(CallResult.Error(t.localizedMessage))
                }
            }
        })
    }

    companion object {
        const val BASE_URL = "http://dev-exam.l-tech.ru/api/v1/"
    }
}