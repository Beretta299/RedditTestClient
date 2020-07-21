package com.beretta.reddittestclient.presentation

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.beretta.redditclient.arch.MvpView
import com.beretta.redditclient.presentation.RedditPresenter
import com.beretta.reddittestclient.R
import com.beretta.reddittestclient.RedditApplication
import javax.inject.Inject

class RedditActivity : AppCompatActivity(), RedditContract.View, MvpView {

    @Inject lateinit var presenter: RedditPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RedditApplication.getAppComponent()?.inject(this)

    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)

    }


    override fun onStop() {
        super.onStop()

        presenter.detachView()
    }




}
