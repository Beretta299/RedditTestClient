package com.beretta.reddittestclient.rest.service

import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditService {

    @GET("top.json")
    fun getRedditPosts(@Query("limit") limit: Long, @Query("after") after: String) : Flowable<ResponseBody>


    companion object {
        private val baseUrl = "https://www.reddit.com/"

        fun createManager() : RedditService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()

            return retrofit.create(RedditService::class.java)
        }
    }
}