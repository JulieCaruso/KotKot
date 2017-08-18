package com.application.kotkot.news

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.application.kotkot.R
import com.application.kotkot.network.News
import com.application.kotkot.network.NewsItem
import com.application.kotkot.utils.ui.InfiniteScrollListener
import io.reactivex.disposables.Disposable

import kotlinx.android.synthetic.main.fragment_news.*

class NewsFragment : Fragment() {

    val newsManager = NewsManager()
    var disposable: Disposable? = null

    var news: News? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.fragment_news, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = NewsAdapter()
        recycler.addOnScrollListener(InfiniteScrollListener({ requestNews() }, recycler.layoutManager as LinearLayoutManager))
        requestNews()
    }

    fun requestNews() {
        var after = news?.after ?: ""
        disposable = newsManager.getNews(after)
                .subscribe(
                        { result ->
                            news = result
                            (recycler.adapter as NewsAdapter).addNews(result.news)

                        },
                        { e ->
                            Snackbar.make(getView()!!, e.message.toString(), Snackbar.LENGTH_LONG).show()
                            Log.d("azerty", e.message.toString())
                        })
    }
}