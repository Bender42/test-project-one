package com.vmatdev.testprojectone.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

//import androidx.security.crypto.EncryptedSharedPreferences

class Settings(context: Context) {

    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    /*private val sharedPreferences: SharedPreferences = SharedPreferencesEncrypted.create(
        PREFERENCES_FILE_NAME,
        MASTER_KEY_ALIAS,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )*/

    fun clearValue(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    fun putValue(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getStringValue(key: String): String? {
        return sharedPreferences.getString(key, "")
    }

    companion object {
        const val AUTH_DATA_KEY = "AUTH_DATA_KEY"

        private const val PREFERENCES_FILE_NAME = "preferences"
        private const val MASTER_KEY_ALIAS = "masterKeyAlias"
    }
}