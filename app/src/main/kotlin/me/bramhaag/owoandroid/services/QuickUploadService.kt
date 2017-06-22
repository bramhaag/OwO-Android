package me.bramhaag.owoandroid.services

import android.app.IntentService
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.preference.PreferenceManager
import android.widget.Toast
import me.bramhaag.owoandroid.R
import me.bramhaag.owoandroid.activities.MainActivity
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.net.URL

class QuickUploadService : IntentService("QuickUploadService") {

    lateinit var mHandler: Handler

    override fun onCreate() {
        super.onCreate()
        mHandler = Handler()
    }

    override fun onHandleIntent(intent: Intent) {
        val uri: Uri = intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)

        sendToast(R.string.toast_uploading)

        var data: ByteArray? = null

        applicationContext.contentResolver.openInputStream(uri).use {
            val buffer = ByteArray(8192)
            var pos: Int

            ByteArrayOutputStream().use { output ->
                while(true) {
                    pos = it.read(buffer)
                    if(pos == -1) {
                        break
                    }

                    output.write(buffer, 0, pos)
                }

                data = output.toByteArray()
            }

        }

        if(data == null) {
            sendToast(R.string.toast_upload_error)
            return
        }

        val requestFile = RequestBody.create(MediaType.parse(applicationContext.contentResolver.getType(uri) ?: "application/octect-stream"), data)
        val requestPart = MultipartBody.Part.createFormData("files[]", null, requestFile)
        val call = MainActivity.owo.service.upload(requestPart)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                Toast.makeText(applicationContext, getString(R.string.toast_upload_failed), Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (call.isCanceled || !response.isSuccessful) {
                    //dialog.dismiss()
                    return
                }

                val obj = JSONObject(response.body()?.string()).getJSONArray("files").getJSONObject(0)
                val url = URL("${PreferenceManager.getDefaultSharedPreferences(applicationContext).getString("pref_destination", "https://owo.whats-th.is")}/${obj.getString("url")}")

                (applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip = ClipData.newPlainText(url.toString(), url.toString())
                sendToast(R.string.toast_uploaded)
            }
        })
    }

    private fun sendToast(resourceId: Int) {
        mHandler.post({
            Toast.makeText(applicationContext, applicationContext.getString(resourceId), Toast.LENGTH_LONG).show()
        })
    }
}


