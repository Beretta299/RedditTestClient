package com.beretta.redditclient.arch

interface MvpPresenter<V: MvpView> {

    fun attachView(mvpView: V?)

    fun detachView()

}