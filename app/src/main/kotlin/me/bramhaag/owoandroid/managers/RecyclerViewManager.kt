package me.bramhaag.owoandroid.managers

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import me.bramhaag.owoandroid.adapters.ShortenUrlAdapter
import me.bramhaag.owoandroid.adapters.UploadFileAdapter
import me.bramhaag.owoandroid.components.ShortenHistoryItem
import me.bramhaag.owoandroid.components.UploadHistoryItem
import me.bramhaag.owoandroid.data.files.FilesDbHelper
import me.bramhaag.owoandroid.data.urls.UrlDbHelper
import java.net.URL
import java.util.*

class RecyclerViewManager(var mUploadView: RecyclerView, var mShortenView: RecyclerView, mContext: Context) {

    val filesDbHelper = FilesDbHelper(mContext)
    val urlDbHelper = UrlDbHelper(mContext)

    val files = filesDbHelper.getFiles().map { UploadHistoryItem(it.first, URL(it.second), it.third) }.toCollection(LinkedList<UploadHistoryItem>())
    val urls = urlDbHelper.getUrls().map { ShortenHistoryItem(it.first, it.second, it.third) }.toCollection(LinkedList<ShortenHistoryItem>())

    init {
        mUploadView.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = UploadFileAdapter(files, mContext)
        }

        mShortenView.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = ShortenUrlAdapter(urls, mContext)
        }
    }

    fun addFile(name: String, url: URL, date: Date) {
        files.addFirst(UploadHistoryItem(name, url, date))
        mUploadView.adapter.notifyItemInserted(0)

        filesDbHelper.addFile(name, url.toString(), date)
    }

    fun addUrl(originalUrl: URL, shortenedUrl: URL, date: Date) {
        urls.addFirst(ShortenHistoryItem(originalUrl, shortenedUrl, date))
        mShortenView.adapter.notifyItemInserted(0)

        urlDbHelper.addUrl(originalUrl.toString(), shortenedUrl.toString(), date)
    }
}

