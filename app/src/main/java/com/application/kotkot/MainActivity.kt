package com.application.kotkot

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.application.kotkot.news.NewsActivity

import kotlinx.android.synthetic.main.activity_main.*;

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        text.setOnClickListener {
            text.setText(text.text.toString() + " again")
        }
        doglist.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java)
            startActivity(intent)
        }
    }
}
