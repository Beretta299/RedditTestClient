package com.beretta.reddittestclient.arch

interface MvpPresenter<V: MvpView> {

    fun attachView(mvpView: V?)

    fun detachView()

}