package com.application.kotkot.news

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.application.kotkot.R

class NewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        supportFragmentManager.beginTransaction()
                .replace(R.id.main_content, NewsFragment())
                .commit()
    }
}
