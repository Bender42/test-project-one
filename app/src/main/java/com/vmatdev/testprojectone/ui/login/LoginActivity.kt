package com.vmatdev.testprojectone.ui.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.vmatdev.testprojectone.R
import com.vmatdev.testprojectone.network.CallResult
import com.vmatdev.testprojectone.network.MyConnector
import com.vmatdev.testprojectone.network.objects.phoneMask.PhoneMaskResponse
import com.vmatdev.testprojectone.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        button.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val connector = MyConnector()
        connector.getPhoneMask {
            when (it) {
                is CallResult.Success -> {
                    it.response as PhoneMaskResponse
                    mask.text = it.response.phoneMask
                }
                is CallResult.Error -> {
                    mask.text = it.error
                }
            }
        }
    }
}
