package com.vmatdev.testprojectone.network.objects.post

import com.vmatdev.testprojectone.network.objects.base.IBaseResponse
import com.vmatdev.testprojectone.network.objects.post.data.PostDto

class PostsResponse(var posts: List<PostDto> = ArrayList()) : IBaseResponse