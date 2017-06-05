package me.bramhaag.owoandroid.data.urls

import android.provider.BaseColumns
import me.bramhaag.owoandroid.data.files.FilesContract

class UrlContract {

    companion object {
        val SQL_CREATE_ENTRIES = "CREATE TABLE ${UrlEntry.TABLE_NAME} (" +
                "${UrlEntry._ID} INTEGER PRIMARY KEY," +
                "${UrlEntry.COLUMN_NAME_ORIGINAL_URL} TEXT," +
                "${UrlEntry.COLUMN_NAME_SHORTENED_URL} TEXT," +
                "${UrlEntry.COLUMN_NAME_DATE} INTEGER)"

        val SQL_DELETE_ENTRY = "DELETE FROM ${FilesContract.FileEntry.TABLE_NAME} WHERE ${FilesContract.FileEntry.COLUMN_NAME_URL} = %s"
        val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${UrlEntry.TABLE_NAME}"

        val SQL_GET_URLS = "SELECT * FROM ${UrlEntry.TABLE_NAME}"
    }

    class UrlEntry : BaseColumns {
        companion object {
            val _ID = BaseColumns._ID
            val TABLE_NAME = "urls"
            val COLUMN_NAME_ORIGINAL_URL = "original_url"
            val COLUMN_NAME_SHORTENED_URL = "shortened_url"
            val COLUMN_NAME_DATE = "date"
        }
    }
}