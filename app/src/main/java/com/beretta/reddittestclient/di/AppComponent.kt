package com.beretta.reddittestclient.di

import com.beretta.reddittestclient.presentation.RedditActivity
import com.beretta.reddittestclient.presentation.di.RedditPresentationModule
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, RedditPresentationModule::class])
@Singleton
interface AppComponent {
    fun inject(activity: RedditActivity)
}