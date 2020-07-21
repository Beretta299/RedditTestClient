package com.beretta.reddittestclient.rest.manager

import com.beretta.reddittestclient.rest.model.RedditPost
import com.beretta.reddittestclient.rest.service.RedditService
import com.google.gson.Gson
import io.reactivex.Flowable
import org.json.JSONObject

class RestManagerImpl(val restService: RedditService) : RestManager {

    val listOfPosts = arrayListOf<RedditPost>()

    override fun loadNewPortion(limit: Int, after: String): Flowable<List<RedditPost>> {
            return restService.getRedditPosts(50, after)
                .flatMap {
                    listOfPosts.clear()
                    val json = JSONObject(it.string())
                    val data = json.getJSONObject("data")
                    val children = data.getJSONArray("children")
                    for(i in 0 until children.length()) {
                        val child = children.getJSONObject(i)
                        val data = child.getJSONObject("data")
                        val gson = Gson()

                        val redditPost : RedditPost = gson.fromJson(data.toString(), RedditPost::class.java)

                        listOfPosts.add(redditPost)
                    }

                    return@flatMap Flowable.fromCallable { listOfPosts }
                }
    }
}