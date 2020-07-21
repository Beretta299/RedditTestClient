package com.beretta.reddittestclient

import android.app.Application
import com.beretta.reddittestclient.di.AppComponent
import com.beretta.reddittestclient.di.AppModule
import com.beretta.reddittestclient.di.DaggerAppComponent


class RedditApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        appComponent = buildAppComponent()
    }


    fun buildAppComponent() : AppComponent {
        return DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    companion object {

        private var appComponent: AppComponent? = null


        fun getAppComponent(): AppComponent?{
            return appComponent
        }
    }
}