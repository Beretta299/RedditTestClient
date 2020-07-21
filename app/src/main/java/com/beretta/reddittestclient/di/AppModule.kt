package com.beretta.reddittestclient.di

import android.content.Context
import com.beretta.reddittestclient.preferences.UserPreferences
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
    fun provideUserPreferences(context: Context) : UserPreferences{
        return UserPreferences(context)
    }
}