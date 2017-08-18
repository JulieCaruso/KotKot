package com.application.kotkot.news

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.application.kotkot.R
import com.application.kotkot.network.NewsItem
import com.application.kotkot.utils.ui.ViewType
import com.application.kotkot.utils.ui.ViewTypeDelegateAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_news.view.*

class NewsDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = NewsViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as NewsViewHolder
        holder.bind(item as NewsItem)

    }

    class NewsViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
    (LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)) {

        fun bind(item: NewsItem) = with(itemView) {
            if (item.thumbnail.isEmpty())
                Picasso.with(itemView.context)
                        .load(R.drawable.babychick)
                        .into(image)
            else
                Picasso.with(itemView.context)
                        .load(item.thumbnail)
                        .into(image)
            title.text = item.title
            author.text = item.author
        }
    }
}