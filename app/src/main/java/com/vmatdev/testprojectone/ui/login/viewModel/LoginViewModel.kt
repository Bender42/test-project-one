package com.vmatdev.testprojectone.ui.login.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.os.Handler
import com.google.gson.Gson
import com.vmatdev.testprojectone.data.AuthData
import com.vmatdev.testprojectone.events.SingleLiveEvents
import com.vmatdev.testprojectone.network.CallResult
import com.vmatdev.testprojectone.network.MyConnector
import com.vmatdev.testprojectone.network.objects.auth.AuthRequest
import com.vmatdev.testprojectone.network.objects.auth.AuthResponse
import com.vmatdev.testprojectone.network.objects.phoneMask.PhoneMaskResponse
import com.vmatdev.testprojectone.utils.CustomMaskImpl
import com.vmatdev.testprojectone.utils.Settings
import com.vmatdev.testprojectone.utils.Settings.Companion.AUTH_DATA_KEY

class LoginViewModel : ViewModel() {

    var connector = MyConnector()

    val progress = SingleLiveEvents<Boolean>()
    val messages = SingleLiveEvents<String>()
    val authData = MutableLiveData<AuthData>()

    fun auth(authData: AuthData, successCallback: () -> Unit) {
        if (authData.phone.isEmpty()) {
            messages.postValue("Не заполнен номер телефона")
            return
        } else if (authData.password.isEmpty()) {
            messages.postValue("Не заполнен пароль")
            return
        }
        progress.postValue(true)
        Handler().postDelayed({
            connector.auth(AuthRequest(authData.phone, authData.password)) {
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

    fun saveAuthData(authData: AuthData, context: Context) {
        val authDataJson = Gson().toJson(authData)
        Settings(context).putValue(AUTH_DATA_KEY, authDataJson)
    }

    fun getSavedAuthData(context: Context): AuthData? {
        val authDataJson = Settings(context).getStringValue(AUTH_DATA_KEY)
        if (authDataJson?.isNotEmpty() == true) {
            return Gson().fromJson<AuthData>(authDataJson, AuthData::class.java)
        } else {
            return null
        }
    }

    fun getNewAuthData(inputPhone: String, inputPassword: String): AuthData {
        val newAuthData = AuthData()
        var phone = inputPhone
        if (phone.isNotEmpty()) {
            authData.value?.let {
                if (it.phoneMask.isNotEmpty()) {
                    newAuthData.phoneMask = it.phoneMask
                    phone = CustomMaskImpl(it.phoneMask).getValue(phone, true)
                }
            }
        }
        newAuthData.phone = phone
        newAuthData.password = inputPassword
        return newAuthData
    }

    fun initAuthData(context: Context) {
        val savedAuthData = getSavedAuthData(context)
        if (savedAuthData != null && savedAuthData.isValid()) {
            authData.postValue(
                AuthData(
                    savedAuthData.phone,
                    savedAuthData.phoneMask,
                    savedAuthData.password
                )
            )
        } else {
            progress.postValue(true)
            Handler().postDelayed({
                connector.getPhoneMask {
                    progress.postValue(false)
                    when (it) {
                        is CallResult.Success -> {
                            it.response as PhoneMaskResponse
                            authData.postValue(AuthData("", it.response.phoneMask, ""))
                        }
                        is CallResult.Error -> {
                            messages.postValue(it.error)
                        }
                    }
                }
            }, 500)
        }
    }
}