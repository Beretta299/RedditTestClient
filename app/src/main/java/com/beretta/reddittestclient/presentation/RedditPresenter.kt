package com.beretta.redditclient.presentation

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import com.beretta.reddittestclient.arch.BasePresenter
import com.beretta.reddittestclient.preferences.UserPreferences
import com.beretta.reddittestclient.presentation.RedditContract
import com.beretta.reddittestclient.rest.manager.RestManager
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class RedditPresenter(private val restManager: RestManager, private val userPreferences: UserPreferences) : RedditContract.Presenter, BasePresenter<RedditContract.View>() {

    val MIN_PRELOAD_VALUE = 20

    val TAG = RedditPresenter::class.simpleName

    override fun loadNews(lastItemName: String) {
        val disposable = restManager.loadNewPortion(50, lastItemName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(it.size.rem(10) == 0) {
                    mvpView?.appendToList(it)
                }
            }, {
                mvpView?.showErrorMessage(it.localizedMessage)
                mvpView?.showDataNotLoaded()
            })
    }

    override fun checkScrollAutoLoad(lastVisiblePosition: Int, lastItemName: String) {
        if((lastVisiblePosition >= (mvpView?.getItemsCount()?.minus(MIN_PRELOAD_VALUE)?:0))){
            loadNews(lastItemName)
        }
    }

    override fun isItemsLoaded() {
        mvpView?.resetList()
        loadNews("")
    }

    override fun loadImage(url: String, target: Target) {
         Picasso
            .get()
            .load(url)
            .into(target)
    }
}