package com.application.kotkot.network

import android.os.Parcel
import android.os.Parcelable
import com.application.kotkot.utils.ui.AdapterConstants
import com.application.kotkot.utils.ui.ViewType

class ApiNewsResponse(val data: ApiNewsDataResponse)

class ApiNewsDataResponse(
        val children: List<ApiNewsChildrenResponse>,
        val after: String?,
        val before: String?
)

class ApiNewsChildrenResponse(val data: ApiNewsResponseNewsItem)

class ApiNewsResponseNewsItem(
        val author: String,
        val title: String,
        val thumbnail: String,
        val url: String?
)

data class News(
        val after: String,
        val before: String,
        val news: List<NewsItem>) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.createTypedArrayList(NewsItem.CREATOR))


    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        parcel?.writeString(after)
        parcel?.writeString(before)
        parcel?.writeTypedList(news)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<News> {
        override fun createFromParcel(parcel: Parcel): News = News(parcel)
        override fun newArray(size: Int): Array<News?> = arrayOfNulls(size)
    }
}

data class NewsItem(
        val title: String,
        val author: String,
        val thumbnail: String,
        val url: String?) : ViewType, Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun getViewType(): Int = AdapterConstants.NEWS

    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        parcel?.writeString(title)
        parcel?.writeString(author)
        parcel?.writeString(thumbnail)
        parcel?.writeString(url)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<NewsItem> {
        override fun createFromParcel(parcel: Parcel): NewsItem = NewsItem(parcel)
        override fun newArray(size: Int): Array<NewsItem?> = arrayOfNulls(size)
    }
}