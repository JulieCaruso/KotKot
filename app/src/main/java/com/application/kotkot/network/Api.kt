package com.application.kotkot.network

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("/hot.json")
    fun getNews(@Query("after") after: String,
                @Query("limit") limit: String): Observable<ApiNewsResponse>

    companion object {
        fun create(): Api {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://www.reddit.com")
                    .build()
            return retrofit.create(Api::class.java)
        }
    }
}
