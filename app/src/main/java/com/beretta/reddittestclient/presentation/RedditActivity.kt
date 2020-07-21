package com.beretta.reddittestclient.presentation

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.beretta.redditclient.presentation.RedditPresenter
import com.beretta.reddittestclient.R
import com.beretta.reddittestclient.RedditApplication
import com.beretta.reddittestclient.arch.MvpView
import com.beretta.reddittestclient.presentation.util.ITitleListener
import com.beretta.reddittestclient.presentation.util.RedditAdapter
import com.beretta.reddittestclient.rest.model.RedditPost
import javax.inject.Inject

class RedditActivity : AppCompatActivity(), RedditContract.View, MvpView, SwipeRefreshLayout.OnRefreshListener {

    @Inject lateinit var presenter: RedditPresenter

    lateinit var recyclerView: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var notLoadedTextView: TextView

    lateinit var adapter: RedditAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RedditApplication.getAppComponent()?.inject(this)

        initViews()

    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)

        presenter.loadNews(adapter.getLastItemName()?:"")
    }


    override fun onStop() {
        super.onStop()

        presenter.detachView()
    }

    override fun appendToList(list: List<RedditPost>) {
        adapter.appendPosts(list)

        swipeRefreshLayout.isRefreshing = false
        notLoadedTextView.visibility = View.GONE
    }

    override fun showErrorMessage(error: String) {
        swipeRefreshLayout.isRefreshing = false
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }


    private fun initViews() {
        recyclerView = findViewById(R.id.rv_reddit_posts_list)
        swipeRefreshLayout = findViewById(R.id.srl_refresh_list)
        notLoadedTextView = findViewById(R.id.tv_not_loaded_data)

        swipeRefreshLayout.setOnRefreshListener(this)

        swipeRefreshLayout.isRefreshing = true

        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = RedditAdapter(this)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        recyclerView.adapter = adapter

        adapter.setLinkListener(object : ITitleListener {
            override fun openUrl(url: String) {
                openUrlInWebView(url)
            }
        })


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                val manager = (recyclerView.layoutManager as LinearLayoutManager)

                presenter.checkScrollAutoLoad(manager.findLastVisibleItemPosition(), adapter.getLastItemName()?:"")
            }
        })


    }

    override fun getItemsCount(): Int {
        return adapter.itemCount
    }

    private fun openUrlInWebView(url: String) {
        val customTabsBuilder = CustomTabsIntent.Builder()

        val customTabsIntent = customTabsBuilder.build()

        customTabsIntent.launchUrl(this, Uri.parse(url))
    }

    override fun resetList() {
        adapter.clearPosts()
    }

    override fun showDataNotLoaded() {
        notLoadedTextView.visibility = View.VISIBLE
    }

    override fun onRefresh() {
        presenter.isItemsLoaded()
    }

}
