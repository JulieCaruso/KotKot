package com.application.kotkot

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.application.kotkot.news.list.NewsActivity
import com.application.kotkot.photo.PhotoActivity
import com.application.kotkot.scan.ScanActivity
import kotlinx.android.synthetic.main.activity_main.*

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
        scan.setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
        }
        photo.setOnClickListener {
            startActivity(Intent(this, PhotoActivity::class.java))
        }
    }
}
