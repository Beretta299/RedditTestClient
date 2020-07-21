package com.beretta.reddittestclient.di

import android.content.Context
import com.beretta.reddittestclient.preferences.UserPreferences
import com.beretta.reddittestclient.rest.manager.RestManager
import com.beretta.reddittestclient.rest.service.RedditService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val context: Context) {

    @Provides
    @Singleton
    fun provideContext() : Context{
        return context
    }

    @Provides
    @Singleton
    fun provideRestService() : RedditService {
        return RedditService.createManager()
    }

    @Provides
    @Singleton
    fun provideRestManager(restService: RedditService): RestManager {
        return RestManager.createRestManager(restService)
    }

    @Provides
    @Singleton
    fun provideUserPreferences(context: Context) : UserPreferences{
        return UserPreferences(context)
    }
}