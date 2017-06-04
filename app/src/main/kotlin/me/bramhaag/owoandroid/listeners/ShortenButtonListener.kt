package me.bramhaag.owoandroid.listeners

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.text.Html
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import me.bramhaag.owoandroid.R
import me.bramhaag.owoandroid.activities.MainActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL
import java.util.*
import java.util.regex.Pattern


class ShortenButtonListener(val activity: MainActivity): View.OnClickListener {

    val URL_PATTERN = Pattern.compile(
            "^(?:(?:https?|ftp)://)(?:\\S+(?::\\S*)?@)?(?:(?!(?:10|127)(?:\\.\\d{1,3}){3})(?!(?:169\\.254|192\\.168)(?:\\.\\d{1,3}){2})(?!172\\.(?:1[6-9]|2\\d|3[0-1])(?:\\.\\d{1,3}){2})(?:[1-9]\\d?|1\\d\\d|2[01]\\d|22[0-3])(?:\\.(?:1?\\d{1,2}|2[0-4]\\d|25[0-5])){2}(?:\\.(?:[1-9]\\d?|1\\d\\d|2[0-4]\\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)(?:\\.(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)*(?:\\.(?:[a-z\u00a1-\uffff]{2,}))\\.?)(?::\\d{2,5})?(?:[/?#]\\S*)?$",
            Pattern.CASE_INSENSITIVE
    )!!

    override fun onClick(v: View?) {
        val shortenDialogBuilder = MaterialDialog.Builder(activity)
                .title("Shorten URL")
                .input("Input URL", null, false, { _, _ -> })
                .negativeText("Cancel")
                .positiveText("Shorten")

        val resultDialogBuilder = MaterialDialog.Builder(activity)
                .title("Shortened URL")
                .content("Loading...")
                .negativeText("Close")
                .neutralText("Copy")

        shortenDialogBuilder.onPositive({ dialog, _ ->
            var url = dialog.inputEditText?.text.toString()

            if(!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("ftp://") && !Patterns.IP_ADDRESS.matcher(url).matches()) {
                url = "http://" + url
            }

            if(!URL_PATTERN.matcher(url).matches()) {
                dialog.builder
                        .content("Invalid URL")
                        .contentColor(Color.RED)
                        .input("Input URL", url, false, { _, _ -> })
                        .show()

                return@onPositive
            }

            val resultDialog = resultDialogBuilder.onNeutral({ _, _ ->
                val input = dialog.inputEditText?.text

                (activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip = ClipData.newPlainText(input, input)
                Toast.makeText(activity, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
            }).show()

            resultDialog.getActionButton(DialogAction.NEUTRAL).isEnabled = false

            activity.owo.service.shorten(url).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(call.isCanceled || !response.isSuccessful) {
                        MaterialDialog.Builder(activity)
                                .title(R.string.error_title)
                                .content(
                                        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                                            Html.fromHtml(activity.getString(R.string.shorten_error_message, url, 200, response.code(), response.errorBody()?.string()), Html.FROM_HTML_MODE_LEGACY)
                                        else
                                            Html.fromHtml(activity.getString(R.string.shorten_error_message, url, 200, response.code(), response.errorBody()?.string()))
                                )
                                .positiveText(R.string.ok)
                                .show()
                        return
                    }

                    val shortenedUrl = response.body()?.string()!!

                    resultDialog.setContent(shortenedUrl)
                    resultDialog.getActionButton(DialogAction.NEUTRAL).isEnabled = true

                    activity.mRecycleViewManager.addUrl(URL(url), URL(shortenedUrl), Date())
                }

                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

            })
        }).show()
    }
}