package com.vmatdev.testprojectone.ui.login.model

class AuthData(
    var phone: String = "",
    var phoneMask: String = "",
    var password: String = ""
) {
    fun isValid(): Boolean {
        return phone.isNotEmpty() && password.isNotEmpty()
    }
}