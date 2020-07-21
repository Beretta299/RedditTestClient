package com.beretta.reddittestclient.presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
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
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject


class RedditActivity : AppCompatActivity(), RedditContract.View, MvpView, SwipeRefreshLayout.OnRefreshListener {

    @Inject lateinit var presenter: RedditPresenter

    lateinit var recyclerView: RecyclerView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var notLoadedTextView: TextView

    lateinit var adapter: RedditAdapter

    val TAG = RedditActivity::class.simpleName

    lateinit var disposeBag: CompositeDisposable

    private val STORAGE_PERMISSIONS = arrayOf( android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RedditApplication.getAppComponent()?.inject(this)

        initViews()
        verifyPermissions()
    }

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)

        presenter.loadNews(adapter.getLastItemName()?:"")

        disposeBag = CompositeDisposable()
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

    fun verifyPermissions() {
        val permissionExternalMemory: Int = ActivityCompat.checkSelfPermission(
            this@RedditActivity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permissionExternalMemory != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@RedditActivity, STORAGE_PERMISSIONS, 1)
        }
    }

    private fun initRecyclerView() {
        adapter = RedditAdapter(this)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        recyclerView.adapter = adapter

        adapter.setLinkListener(object : ITitleListener {
            override fun openUrl(url: String?) {
                openUrlInWebView(url?:"")
            }

            override fun saveImage(url: String?) {
                saveImageToGallery(url?:"")
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

    private fun saveImageToGallery(url: String) {
        presenter?.loadImage(url, target)
    }

    private val target = object : Target {
        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            //TODO
        }

        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
            Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
        }

        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            disposeBag.add(Single.fromCallable {
                return@fromCallable getImageUri(bitmap)
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    shareImage(it)
                }, {
                    Log.e(TAG, it.localizedMessage)
                }))
        }

    }

    private fun shareImage(uri: Uri?) {
        if(uri != null) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.type = "image/png"
            startActivity(intent)
        }
    }

    private fun getImageUri(bitmap: Bitmap?) : Uri?{
        val imagesFolder = File(cacheDir, "images")
        var uri: Uri? = null
        try {
            imagesFolder.mkdirs()
            val file = File(imagesFolder, "shared_image.png")
            val stream = FileOutputStream(file)
            bitmap?.compress(Bitmap.CompressFormat.PNG, 90, stream)
            stream.flush()
            stream.close()
            uri = FileProvider.getUriForFile(this, "com.beretta.fileprovider", file)
        } catch (e: IOException) {
            Log.e(TAG, "IOException while trying to write file for sharing: " + e.localizedMessage)
        }
        return uri!!
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
