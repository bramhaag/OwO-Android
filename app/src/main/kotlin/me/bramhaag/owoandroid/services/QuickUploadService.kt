package me.bramhaag.owoandroid.services

import android.app.IntentService
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Handler
import android.preference.PreferenceManager
import android.support.v4.app.NotificationCompat
import me.bramhaag.owoandroid.R
import me.bramhaag.owoandroid.activities.MainActivity
import me.bramhaag.owoandroid.api.ProgressRequestBody
import me.bramhaag.owoandroid.util.ByteUnit
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.net.URL
import java.text.DecimalFormat
import java.util.concurrent.atomic.AtomicInteger

class QuickUploadService : IntentService("QuickUploadService") {

    companion object {
        private val mDecimalFormat = DecimalFormat("#.##")
        private val atomicInteger = AtomicInteger(0)
    }

    lateinit var mHandler: Handler

    override fun onCreate() {
        super.onCreate()
        mHandler = Handler()
    }

    override fun onHandleIntent(intent: Intent) {
        val uri: Uri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
        var data: ByteArray? = null

        val id = atomicInteger.incrementAndGet()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(applicationContext)
                .setOngoing(true)
                .setSmallIcon(R.drawable.upload)
                .setContentTitle("Uploading...")
                .setProgress(100, 0, true)

        val errorNotification = NotificationCompat.Builder(applicationContext)
                .setContentTitle("Error!")
                .setContentText("An error occurred while uploading your file!")

        notificationManager.notify(id, notification.build())

        applicationContext.contentResolver.openInputStream(uri).use {
            val buffer = ByteArray(8192)
            var pos: Int

            ByteArrayOutputStream().use { output ->
                while (true) {
                    pos = it.read(buffer)
                    if (pos == -1) break

                    output.write(buffer, 0, pos)
                }

                data = output.toByteArray()
            }
        }

        if (data == null) {
            notificationManager.notify(id, errorNotification.build())
            return
        }

        val requestFile = ProgressRequestBody(uri, applicationContext, object : ProgressRequestBody.ProgressConsumer {
            override fun accept(progress: Double, max: Double, byteUnit: ByteUnit) {
                notification.setProgress(max.toInt(), progress.toInt(), false)
                notification.setContentText("${mDecimalFormat.format(byteUnit.convert(progress, ByteUnit.BYTE))} ${byteUnit.unit} / ${mDecimalFormat.format(byteUnit.convert(max, ByteUnit.BYTE))} ${byteUnit.unit}")

                notificationManager.notify(id, notification.build())
            }
        })

        val requestPart = MultipartBody.Part.createFormData("files[]", requestFile.name, requestFile)

        MainActivity.owo.service.upload(requestPart).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                notificationManager.notify(id, errorNotification.build())
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (call.isCanceled || !response.isSuccessful) {
                    notificationManager.notify(id, errorNotification.build())
                    return
                }

                val obj = JSONObject(response.body()?.string()).getJSONArray("files").getJSONObject(0)
                val url = URL("${PreferenceManager.getDefaultSharedPreferences(applicationContext).getString("pref_destination", "https://owo.whats-th.is")}/${obj.getString("url")}")

                (applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip = ClipData.newPlainText(url.toString(), url.toString())

                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()))
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

                notificationManager.notify(id, NotificationCompat.Builder(applicationContext)
                        .setSmallIcon(R.drawable.owo_white)
                        .setContentTitle("Upload completed!")
                        .setColor(Color.rgb(40, 153, 217))
                        .setContentIntent(PendingIntent.getActivity(applicationContext, 0, browserIntent, 0))
                        .setAutoCancel(true)
                        .build()
                )
            }
        })
    }
}


