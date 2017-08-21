package com.application.kotkot.news.list

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v4.util.SparseArrayCompat
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.application.kotkot.R
import com.application.kotkot.network.NewsItem
import com.application.kotkot.utils.ui.AdapterConstants
import com.application.kotkot.utils.ui.LoadingDelegateAdapter
import com.application.kotkot.utils.ui.ViewType
import com.application.kotkot.utils.ui.ViewTypeDelegateAdapter

class NewsAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: ArrayList<ViewType>

    private var delegateAdapters = SparseArrayCompat<ViewTypeDelegateAdapter>()

    private val loadingItem = object : ViewType {
        override fun getViewType(): Int = AdapterConstants.LOADING
    }

    init {
        delegateAdapters.put(AdapterConstants.LOADING, LoadingDelegateAdapter())
        delegateAdapters.put(AdapterConstants.NEWS, NewsDelegateAdapter(context))
        items = ArrayList()
        items.add(loadingItem)
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = items.get(position).getViewType()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, items.get(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            delegateAdapters.get(viewType).onCreateViewHolder(parent)

    fun addNews(news: List<ViewType>) {
        val initPos = items.size - 1
        items.removeAt(initPos)
        items.addAll(news)
        items.add(loadingItem)
        notifyItemRangeChanged(initPos, initPos + news.size)
    }

    fun clearAndAddNews(news: List<ViewType>) {
        items.clear()
        items.addAll(news)
        items.add(loadingItem)
        notifyDataSetChanged()
    }

    fun getNews(): List<NewsItem> {
        return items.filter { it.getViewType() == AdapterConstants.NEWS }
                .map { it as NewsItem }
    }
}