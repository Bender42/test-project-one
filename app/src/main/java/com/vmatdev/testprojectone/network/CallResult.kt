package com.vmatdev.testprojectone.network

import com.vmatdev.testprojectone.network.objects.base.IBaseResponse

sealed class CallResult {

    data class Success(val response: IBaseResponse): CallResult()
    data class Error(val error: String): CallResult()
}