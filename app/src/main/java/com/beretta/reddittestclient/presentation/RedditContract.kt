package com.beretta.reddittestclient.presentation

import android.graphics.Bitmap
import android.net.Uri
import com.beretta.reddittestclient.arch.MvpPresenter
import com.beretta.reddittestclient.arch.MvpView
import com.beretta.reddittestclient.rest.model.RedditPost
import com.squareup.picasso.Target

interface RedditContract {

    interface View : MvpView {
        fun appendToList(list: List<RedditPost>)

        fun showErrorMessage(error: String)

        fun getItemsCount(): Int

        fun resetList()

        fun showDataNotLoaded()
    }

    interface Presenter : MvpPresenter<View> {
        fun loadNews(lastItemName: String)

        fun checkScrollAutoLoad(lastVisibleItem: Int, lastItemName: String)

        fun isItemsLoaded()

        fun loadImage(url: String, target: Target)
    }

}