package com.beretta.reddittestclient.presentation.util

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.beretta.reddittestclient.R
import com.beretta.reddittestclient.rest.model.RedditPost
import com.squareup.picasso.Picasso
import java.lang.StringBuilder
import java.util.*

class RedditAdapter(val context: Context) : RecyclerView.Adapter<RedditAdapter.PostViewHolder>() {

    var posts = arrayListOf<RedditPost?>()

    private var listener : ITitleListener? = null

    fun setLinkListener(listener: ITitleListener){
        this.listener = listener
    }

    fun appendPosts(list: List<RedditPost>) {
        posts.addAll(list)
        notifyDataSetChanged()
    }

    fun getLastItemName() : String? {
        if(posts.size > 0)
            return posts[posts.size - 1]?.name
        else return ""
    }

    fun clearPosts() {
        posts.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.reddit_post_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bindViewHolder(posts[position])
    }

    inner class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val postTitle: TextView = view.findViewById(R.id.tv_post_title)

        val postThumbs: TextView = view.findViewById(R.id.tv_post_thumbsups)

        val postTime: TextView = view.findViewById(R.id.tv_post_time)

        val postSubReddit: TextView = view.findViewById(R.id.tv_post_subreddit)

        val postAuthor: TextView = view.findViewById(R.id.tv_post_author)

        val numberOfComments: TextView = view.findViewById(R.id.tv_post_comments_number)

        val postImage: ImageView = view.findViewById(R.id.iv_post_image)

        fun bindViewHolder(post: RedditPost?) {
            val spannableString = SpannableString(post?.title)

            spannableString.setSpan(ForegroundColorSpan(context.getColor(R.color.linkColor)), 0, post?.title?.length?:0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            postTitle.text = spannableString

            postThumbs.text = post?.ups

            postTime.text = setUpPostDate(post?.created_utc)

            postSubReddit.text = setUpSubReddit(post?.subreddit)

            postAuthor.text = setUpAuthor(post?.author)

            numberOfComments.text = setUpNumberOfComments(post?.num_comments)

            Picasso.get()
                .load(post?.url)
                .into(postImage)

            postTitle.setOnClickListener {
                val stringBuilder = StringBuilder()
                stringBuilder.append("https://www.reddit.com")
                stringBuilder.append(post?.permalink)

                listener?.openUrl(stringBuilder.toString())
            }
        }

        private fun setUpNumberOfComments(numComments: Long?): String {
            val stringBuilder = StringBuilder()
            if(numComments?.rem(1000) != numComments){
                stringBuilder.append(((numComments?: 0)/1000))
                stringBuilder.append("k")
            } else {
                stringBuilder.append(numComments)
            }

            return stringBuilder.toString()
        }

        private fun setUpAuthor(author: String?): String {
            val stringBuilder = StringBuilder()

            stringBuilder.append(context.getString(R.string.author_prefix))
            stringBuilder.append(" ")
            stringBuilder.append(author)

            return stringBuilder.toString()
        }

        private fun setUpSubReddit(subreddit: String?) : Spannable {
            val stringBuilder = StringBuilder()
            val subPrefix = context.getString(R.string.subreddit_prefix)
            stringBuilder.append(subPrefix)
            stringBuilder.append(" ")
            stringBuilder.append(subreddit)

            val spannableStringBuilder = SpannableStringBuilder(stringBuilder)

            spannableStringBuilder.setSpan(ForegroundColorSpan(context.getColor(R.color.linkColor)), subPrefix.length, stringBuilder.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            return spannableStringBuilder
        }

        private fun setUpPostDate(timestamp: Long?) : String {
            val delta = System.currentTimeMillis() - ((timestamp?:0) * 1000)
            val calendar = Calendar.getInstance()

            calendar.time = Date(delta)

            val stringBuilder = StringBuilder()



            when(calendar.get(Calendar.HOUR)) {
                0 ->{
                    stringBuilder.append(context.getString(R.string.date_zero_suffix))
                }
                1 ->{
                    stringBuilder.append(calendar.get(Calendar.HOUR))

                    stringBuilder.append(" ")

                    stringBuilder.append(context.getString(R.string.date_one_suffix))
                }
                else ->{
                    stringBuilder.append(calendar.get(Calendar.HOUR))

                    stringBuilder.append(" ")

                    stringBuilder.append(context.getString(R.string.date_suffix))
                }
            }
            return stringBuilder.toString()
        }

    }
}