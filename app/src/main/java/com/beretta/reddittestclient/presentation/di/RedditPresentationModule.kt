package com.beretta.reddittestclient.presentation.di

import com.beretta.redditclient.presentation.RedditPresenter
import com.beretta.reddittestclient.preferences.UserPreferences
import com.beretta.reddittestclient.rest.manager.RestManager
import dagger.Module
import dagger.Provides

@Module
class RedditPresentationModule {

    @Provides
    fun provideRedditPresenter(restManager: RestManager, userPreferences: UserPreferences) : RedditPresenter {
        return RedditPresenter(restManager, userPreferences)
    }
}