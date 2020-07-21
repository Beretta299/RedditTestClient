package com.beretta.reddittestclient.presentation

import androidx.recyclerview.widget.LinearLayoutManager
import com.beretta.redditclient.arch.MvpPresenter
import com.beretta.redditclient.arch.MvpView

interface RedditContract {

    interface View : MvpView{

    }

    interface Presenter : MvpPresenter<View>{

    }

}