package com.beretta.redditclient.arch


open class BasePresenter<T : MvpView> :  MvpPresenter<T>{

    var mvpView: T? = null

    override fun attachView(mvpView: T?) {
        this.mvpView = mvpView
    }

    override fun detachView() {
        this.mvpView = null
    }

    fun isViewAttached(): Boolean{
        return mvpView != null
    }

}