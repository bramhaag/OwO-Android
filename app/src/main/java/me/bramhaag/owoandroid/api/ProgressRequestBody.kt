package me.bramhaag.owoandroid.api

import android.content.Context
import android.os.Handler
import android.os.Looper
import me.bramhaag.owoandroid.util.ByteUnit
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.IOException

class ProgressRequestBody(private val uploadObject: UploadObject, mContext: Context, private val mProgressConsumer: ProgressConsumer) : RequestBody() {

    override fun contentType() = uploadObject.contentType

    @Throws(IOException::class)
    override fun contentLength() = uploadObject.size

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val buffer = ByteArray(2048)
        val handler = Handler(Looper.getMainLooper())
        val mByteUnit = when (contentLength()) {
            in ByteUnit.C_B..ByteUnit.C_KB - 1 -> ByteUnit.BYTE
            in ByteUnit.C_KB..ByteUnit.C_MB - 1 -> ByteUnit.KB
            else -> ByteUnit.MB
        }

        var uploaded: Double = 0.0

       uploadObject.data.use {
            var read: Int

            while (true) {
                read = it.read(buffer)
                if (read == -1) break

                uploaded += read.toLong()
                sink.write(buffer, 0, read)

                handler.post({
                    mProgressConsumer.accept(uploaded, contentLength().toDouble(), mByteUnit)
                })
            }

           it.close()
        }
    }

    interface ProgressConsumer {
        fun accept(progress: Double, max: Double, byteUnit: ByteUnit)
    }
}