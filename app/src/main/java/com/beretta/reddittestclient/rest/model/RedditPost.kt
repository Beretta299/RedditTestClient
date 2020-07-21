package com.beretta.reddittestclient.rest.model

data class RedditPost(val title: String, val author: String, val created_utc: Long, val url: String, val permalink: String, val ups: String, val num_comments: Long, val subreddit: String, val name: String)