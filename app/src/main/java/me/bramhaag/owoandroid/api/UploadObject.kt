package me.bramhaag.owoandroid.api

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import okhttp3.MediaType
import java.io.ByteArrayInputStream
import java.io.InputStream


class UploadObject {

    val data: InputStream
    val contentType: MediaType
    val name: String
    val size: Long

    constructor(uri: Uri, context: Context) {
        data = context.contentResolver.openInputStream(uri)
        contentType = MediaType.parse(context.contentResolver.getType(uri) ?: "application/octect-stream")!!

        var name = "Unknown"
        var size = 0L
        context.contentResolver.query(uri, null, null, null, null).use {
            it.moveToFirst()

            name = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            size = it.getLong(it.getColumnIndex(OpenableColumns.SIZE))
        }

        this.name = name
        this.size = size
    }

    constructor(data: ByteArray, name: String, contentType: String) {
        this.data = ByteArrayInputStream(data)
        this.contentType = MediaType.parse(contentType)!!
        this.name = name
        this.size = data.size.toLong()
    }
}