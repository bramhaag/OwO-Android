package me.bramhaag.owoandroid.api

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.OpenableColumns
import me.bramhaag.owoandroid.util.ByteUnit
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.IOException

class ProgressRequestBody(private val mUri: Uri, mContext: Context, private val mProgressConsumer: ProgressConsumer) : RequestBody() {

    private val mContentResolver: ContentResolver = mContext.contentResolver

    var name: String = "Unknown"
    var size: Long = 0

    init {
        mContentResolver.query(mUri, null, null, null, null).use {
            it.moveToFirst()

            name = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            size = it.getLong(it.getColumnIndex(OpenableColumns.SIZE))
        }
    }

    override fun contentType() = MediaType.parse(mContentResolver.getType(mUri) ?: "application/octect-stream")

    @Throws(IOException::class)
    override fun contentLength() = size

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val buffer = ByteArray(2048)
        val handler = Handler(Looper.getMainLooper())
        val mByteUnit = when (contentLength()) {
            in ByteUnit.C_B ..ByteUnit.C_KB - 1 -> ByteUnit.BYTE
            in ByteUnit.C_KB..ByteUnit.C_MB - 1 -> ByteUnit.KB
            else -> ByteUnit.MB
        }

        var uploaded: Double = 0.0

        mContentResolver.openInputStream(mUri).use { input ->
            var read: Int

            while (true) {
                read = input.read(buffer)
                if (read == -1) break

                uploaded += read.toLong()
                sink.write(buffer, 0, read)

                handler.post({
                    mProgressConsumer.accept(uploaded, contentLength().toDouble(), mByteUnit)
                })
            }
        }
    }

    interface ProgressConsumer {
        fun accept(progress: Double, max: Double, byteUnit: ByteUnit)
    }
}