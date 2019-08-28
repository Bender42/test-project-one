package com.vmatdev.testprojectone.ui.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.vmatdev.testprojectone.R
import com.vmatdev.testprojectone.ui.login.viewModel.LoginViewModel
import com.vmatdev.testprojectone.ui.main.MainActivity
import com.vmatdev.testprojectone.utils.CustomMaskImpl
import com.vmatdev.testprojectone.utils.KeyboardUtils
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this)[LoginViewModel::class.java]
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        button.setOnClickListener {
            KeyboardUtils.closeKeyboard(this)
            var phone = phone.text.toString()
            if (phone.isNotEmpty()) {
                viewModel.phoneMask.value?.let {
                    phone = CustomMaskImpl(it).getValue(phone, true)
                }
            }
            viewModel.auth(phone, password.text.toString()) {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
        viewModel.phoneMask.observe(this, Observer {
            it?.let {
                phone.hint = it
                CustomMaskImpl(it).installOn(phone)
            }
        })
        viewModel.messages.observe(this, Observer {
            it?.let { Snackbar.make(phone, it, Snackbar.LENGTH_LONG).show() }
        })
        viewModel.progress.observe(this, Observer {
            if (it == true) {
                progress_bar.visibility = View.VISIBLE
            } else {
                progress_bar.visibility = View.GONE
            }
        })

    }
}
