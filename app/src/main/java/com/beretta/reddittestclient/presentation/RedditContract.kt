package com.beretta.reddittestclient.presentation

import com.beretta.reddittestclient.arch.MvpPresenter
import com.beretta.reddittestclient.arch.MvpView
import com.beretta.reddittestclient.rest.model.RedditPost

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
    }

}