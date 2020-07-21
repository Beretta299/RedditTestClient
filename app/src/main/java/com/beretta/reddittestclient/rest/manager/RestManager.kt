package com.beretta.reddittestclient.rest.manager

import com.beretta.reddittestclient.rest.model.RedditPost
import com.beretta.reddittestclient.rest.service.RedditService
import io.reactivex.Flowable

interface RestManager {

    fun loadNewPortion(limit: Int,after: String): Flowable<List<RedditPost>>

    companion object{

        fun createRestManager(redditService: RedditService) : RestManager{
            return RestManagerImpl(redditService)
        }
    }
}