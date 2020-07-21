package com.beretta.reddittestclient.di

import com.beretta.reddittestclient.presentation.RedditActivity
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {
    fun inject(activity: RedditActivity)
}