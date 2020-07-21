package com.beretta.reddittestclient.preferences

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(val context: Context) {
    private val USER_PREFERENCES = "REDDIT_USER_PREFERENCES"
    private val CURRENT_SUBREDDIT = "CURRENT_SUBREDDIT"

    fun getCurrentSubreddit() : String?{
        return getPreferences().getString(CURRENT_SUBREDDIT, null)
    }

    fun setCurrentSubReddit(subReddit: String) {
        getPreferences().edit()
            .putString(CURRENT_SUBREDDIT, subReddit)
    }

    fun getPreferences() : SharedPreferences{
        return context.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE)
    }
}