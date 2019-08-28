package com.vmatdev.testprojectone.utils

import android.app.Activity
import android.view.inputmethod.InputMethodManager

object KeyboardUtils {

    fun closeKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        activity.currentFocus?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }
}