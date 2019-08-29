package com.vmatdev.testprojectone.data

class AuthData(
    var phone: String = "",
    var phoneMask: String = "",
    var password: String = ""
) {
    fun isValid(): Boolean {
        return phone.isNotEmpty() && password.isNotEmpty()
    }
}