package com.vmatdev.testprojectone.ui.login.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Handler
import android.support.design.widget.Snackbar
import com.vmatdev.testprojectone.events.SingleLiveEvents
import com.vmatdev.testprojectone.network.CallResult
import com.vmatdev.testprojectone.network.MyConnector
import com.vmatdev.testprojectone.network.objects.auth.AuthRequest
import com.vmatdev.testprojectone.network.objects.auth.AuthResponse
import com.vmatdev.testprojectone.network.objects.phoneMask.PhoneMaskResponse
import com.vmatdev.testprojectone.network.objects.post.PostsResponse
import com.vmatdev.testprojectone.network.objects.post.data.PostDto
import com.vmatdev.testprojectone.ui.main.model.SortType
import com.vmatdev.testprojectone.utils.CustomMaskImpl
import kotlinx.android.synthetic.main.activity_login.*

class LoginViewModel : ViewModel() {

    var connector = MyConnector()

    val progress = SingleLiveEvents<Boolean>()
    val messages = SingleLiveEvents<String>()
    val phoneMask: MutableLiveData<String> by lazy {
        MutableLiveData<String>().also {
            getPhoneMask()
        }
    }

    fun auth(phone: String, password: String, successCallback: () -> Unit) {
        if (phone.isEmpty()) {
            messages.postValue("Не заполнен номер телефона")
            return
        } else if (password.isEmpty()) {
            messages.postValue("Не заполнен пароль")
            return
        }
        progress.postValue(true)
        Handler().postDelayed({
            connector.auth(AuthRequest(phone, password)) {
                progress.postValue(false)
                when (it) {
                    is CallResult.Success -> {
                        it.response as AuthResponse
                        if (it.response.success) {
                            successCallback.invoke()
                        } else {
                            messages.postValue("Неверный логин или пароль")
                        }
                    }
                    is CallResult.Error -> {
                        messages.postValue("Неверный логин или пароль")
                    }
                }
            }
        }, 500)
    }

    private fun getPhoneMask() {
        progress.postValue(true)
        Handler().postDelayed({
            connector.getPhoneMask {
                progress.postValue(false)
                when (it) {
                    is CallResult.Success -> {
                        it.response as PhoneMaskResponse
                        phoneMask.postValue(it.response.phoneMask)
                    }
                    is CallResult.Error -> {
                        messages.postValue(it.error)
                    }
                }
            }
        }, 500)
    }
}