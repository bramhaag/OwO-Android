package me.bramhaag.owoandroid.data.files

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import me.bramhaag.owoandroid.data.files.FilesContract.Companion.SQL_CREATE_ENTRIES
import me.bramhaag.owoandroid.data.files.FilesContract.Companion.SQL_GET_FILES
import me.bramhaag.owoandroid.data.files.FilesContract.FileEntry.Companion.TABLE_NAME
import java.util.*

class FilesDbHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "files.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun addFile(name: String, url: String, date: Date) {
        readableDatabase.insert(TABLE_NAME, null, ContentValues().apply {
            put(FilesContract.FileEntry.COLUMN_NAME_NAME, name)
            put(FilesContract.FileEntry.COLUMN_NAME_URL, url)
            put(FilesContract.FileEntry.COLUMN_NAME_DATE, date.time)
        })
    }

    fun getFiles() = mutableListOf<Triple<String, String, Date>>().apply {
            readableDatabase.rawQuery(SQL_GET_FILES, null).use {
                if(!it.moveToFirst()) return@apply

                do add(Triple(
                        it.getString(it.getColumnIndex(FilesContract.FileEntry.COLUMN_NAME_NAME)),
                        it.getString(it.getColumnIndex(FilesContract.FileEntry.COLUMN_NAME_URL)),
                        Date(it.getLong(it.getColumnIndex(FilesContract.FileEntry.COLUMN_NAME_DATE)))
                ))
                while (it.moveToNext())
            }

            reverse()
        }
}


