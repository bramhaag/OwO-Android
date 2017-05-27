package me.bramhaag.owoandroid.data.urls

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.net.URL
import java.util.*

class UrlDbHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "urls.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(UrlContract.SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun addUrl(originalUrl: String, shortenedUrl: String, date: Date) = readableDatabase.insert(UrlContract.UrlEntry.TABLE_NAME, null, ContentValues().apply {
                put(UrlContract.UrlEntry.COLUMN_NAME_ORIGINAL_URL, originalUrl)
                put(UrlContract.UrlEntry.COLUMN_NAME_SHORTENED_URL, shortenedUrl)
                put(UrlContract.UrlEntry.COLUMN_NAME_DATE, date.time)
            })

    fun getUrls() = mutableListOf<Triple<URL, URL, Date>>().apply {
        readableDatabase.rawQuery(UrlContract.SQL_GET_URLS, null).use {
            if(!it.moveToFirst()) return mutableListOf<Triple<URL, URL, Date>>()

            do add(Triple(
                    URL(it.getString(it.getColumnIndex(UrlContract.UrlEntry.COLUMN_NAME_ORIGINAL_URL))),
                    URL(it.getString(it.getColumnIndex(UrlContract.UrlEntry.COLUMN_NAME_SHORTENED_URL))),
                    Date(it.getLong(it.getColumnIndex(UrlContract.UrlEntry.COLUMN_NAME_DATE)))
            ))
            while (it.moveToNext())
        }

        reverse()
    }
}


