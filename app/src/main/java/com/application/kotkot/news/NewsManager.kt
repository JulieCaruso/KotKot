package com.application.kotkot.news

import com.application.kotkot.network.Api
import com.application.kotkot.network.News
import com.application.kotkot.network.NewsItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NewsManager {

    val api by lazy {
        Api.create()
    }

    fun getNews(after: String, limit: String = "10"): Observable<News> {
        return Observable.create { subscriber ->
            api.getNews(after, limit)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { response ->
                                val news = News(
                                        response.data.after ?: "",
                                        response.data.before ?: "",
                                        response.data.children.map {
                                            NewsItem(it.data.title, it.data.author, it.data.thumbnail, it.data.url)
                                        })
                                subscriber.onNext(news)
                                subscriber.onComplete()


                            },
                            { e ->
                                subscriber.onError(e)
                            })
        }
    }
}