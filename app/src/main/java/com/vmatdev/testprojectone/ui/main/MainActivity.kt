package com.vmatdev.testprojectone.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.widget.FrameLayout
import com.vmatdev.testprojectone.R

class MainActivity : FragmentActivity() {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<FrameLayout>(R.id.fragment_container)?.let {
            if (savedInstanceState != null) {
                return;
            }
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, PostListFragment()).commit()
        }
    }

    fun onPostSelected(id: String) {
        viewModel.selectedPostId = id
        val postInfoFragment = supportFragmentManager.findFragmentById(R.id.post_info_container)
        if (postInfoFragment != null && postInfoFragment.isAdded) {
            postInfoFragment as PostInfoFragment
            postInfoFragment.updatePostView()
        } else {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, PostInfoFragment())
                    .addToBackStack(null)
                    .commit()
        }
    }
}
