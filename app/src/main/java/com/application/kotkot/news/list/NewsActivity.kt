package com.application.kotkot.news.list

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.application.kotkot.R

class NewsActivity : AppCompatActivity() {

    val TAG_NEWS = "NEWS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        if (supportFragmentManager.findFragmentByTag(TAG_NEWS) == null)
            supportFragmentManager.beginTransaction()
                    .replace(R.id.main_content, NewsFragment(), TAG_NEWS)
                    .commit()
    }
}
