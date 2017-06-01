package me.bramhaag.owoandroid.listeners

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.widget.Toast
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import me.bramhaag.owoandroid.activities.MainActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL
import java.util.*


class ShortenButtonListener(val activity: MainActivity): View.OnClickListener {

    override fun onClick(v: View?) {
        MaterialDialog.Builder(activity)
                .title("Shorten URL")
                .input("Input URL", null, false, { _, _ -> })
                .negativeText("Cancel")
                .positiveText("Shorten")
                .onPositive({ dialog, which ->
                    dialog.dismiss()

                    val resultDialog = MaterialDialog.Builder(activity)
                            .title("Shortened URL")
                            .content("Loading...")
                            .negativeText("Close")
                            .neutralText("Copy")
                            .onNeutral({ dialog, which ->
                                val input = dialog.contentView?.text
                                (activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip = ClipData.newPlainText(input, input)
                                Toast.makeText(activity, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
                            })
                            .show()

                    resultDialog.getActionButton(DialogAction.NEUTRAL).isEnabled = false

                    val originalUrl = dialog.inputEditText?.text.toString()
                    activity.owo.service.shorten(originalUrl).enqueue(object : Callback<ResponseBody> {
                        //TODO checks
                        override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                            println("code: ${response?.code()}")
                            println("error: ${response?.errorBody()?.string()}")
                            val shortenedUrl = response?.body()?.string()
                            resultDialog.setContent(shortenedUrl)
                            resultDialog.getActionButton(DialogAction.NEUTRAL).isEnabled = true
                            activity.mRecycleViewManager.addUrl(URL(originalUrl), URL(shortenedUrl), Date())
                        }

                        override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                    })
                }).show()

    }
}