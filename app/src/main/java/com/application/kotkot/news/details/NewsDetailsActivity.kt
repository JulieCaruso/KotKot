package com.application.kotkot.news.details

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.application.kotkot.R
import com.application.kotkot.network.NewsItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_news_details.*

class NewsDetailsActivity : AppCompatActivity() {

    companion object {
        val EXTRA_NEWS = "EXTRA_NEWS"
    }

    var news: NewsItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_details)

        if (intent != null && intent.hasExtra(EXTRA_NEWS))
            news = intent.getParcelableExtra(EXTRA_NEWS)

        init()
    }

    private fun init() {
        Picasso.with(this)
                .load(news?.url)
                .error(R.drawable.babychick)
                .into(image)

        author.text = news?.author
        description.text = news?.title
    }
}
