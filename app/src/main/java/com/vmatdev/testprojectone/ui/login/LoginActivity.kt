package com.vmatdev.testprojectone.ui.login

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.vmatdev.testprojectone.R
import com.vmatdev.testprojectone.ui.login.viewModel.LoginViewModel
import com.vmatdev.testprojectone.ui.main.MainActivity
import com.vmatdev.testprojectone.utils.CustomMaskImpl
import com.vmatdev.testprojectone.utils.KeyboardUtils
import com.vmatdev.testprojectone.utils.Settings
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this)[LoginViewModel::class.java]
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        save_auth_data.isChecked = viewModel.getSavedAuthData(this)?.isValid() ?: false
        button.setOnClickListener {
            KeyboardUtils.closeKeyboard(this)
            val newAuthData = viewModel.getNewAuthData(phone.text.toString(), password.text.toString())
            viewModel.auth(newAuthData) {
                if (save_auth_data.isChecked) {
                    viewModel.saveAuthData(newAuthData, this)
                } else {
                    Settings(this).clearValue(Settings.AUTH_DATA_KEY)
                }
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
        help.setOnClickListener {
            if (help_content.visibility == View.GONE) {
                help_content.visibility = View.VISIBLE
                help.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_close))
            } else {
                help.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_help))
                help_content.visibility = View.GONE
            }
        }
        viewModel.authData.observe(this, Observer {
            it?.let {
                if (it.phoneMask.isNotEmpty()) {
                    phone.hint = it.phoneMask
                    CustomMaskImpl(it.phoneMask).installOn(phone)
                }
                if (it.phone.isNotEmpty()) {
                    phone.setText(it.phone)
                }
                if (it.password.isNotEmpty()) {
                    password.setText(it.password)
                }
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
        viewModel.initAuthData(this)
    }
}
