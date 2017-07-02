package me.bramhaag.owoandroid.listeners

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.text.Html
import android.util.Log
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

    override fun onClick(v: View) {
        val shortenDialogBuilder = MaterialDialog.Builder(activity)
                .title(v.context.getString(R.string.dialog_shorten_title))
                .input(v.context.getString(R.string.dialog_shorten_input), null, false, { _, _ -> })
                .negativeText(v.context.getString(R.string.dialog_button_cancel))
                .positiveText(v.context.getString(R.string.dialog_button_shorten))

        val resultDialogBuilder = MaterialDialog.Builder(activity)
                .title(v.context.getString(R.string.dialog_shorten_result_title))
                .content(v.context.getString(R.string.dialog_shorten_result_content))
                .negativeText(v.context.getString(R.string.dialog_button_close))
                .neutralText(v.context.getString(R.string.dialog_button_copy))

        shortenDialogBuilder.onPositive({ dialog, _ ->
            var url = dialog.inputEditText?.text.toString()

            if(!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("ftp://") && !Patterns.IP_ADDRESS.matcher(url).matches()) {
                url = "http://" + url
            }

            if(!URL_PATTERN.matcher(url).matches()) {
                dialog.builder
                        .content(v.context.getString(R.string.dialog_shorten_error_content))
                        .contentColor(Color.RED)
                        .input(v.context.getString(R.string.dialog_shorten_input), url, false, { _, _ -> })
                        .show()

                return@onPositive
            }

            val resultDialog = resultDialogBuilder.onNeutral({ _, _ ->
                val input = dialog.inputEditText?.text

                (activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip = ClipData.newPlainText(input, input)
                Toast.makeText(activity, v.context.getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show()
            }).show()

            resultDialog.getActionButton(DialogAction.NEUTRAL).isEnabled = false

            MainActivity.owo.service.shorten(url).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(call.isCanceled || !response.isSuccessful) {
                        @Suppress("DEPRECATION")
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

                    MainActivity.mRecycleViewManager.addUrl(URL(url), URL(shortenedUrl), Date())
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("Shorten error:", t.message)
                    t.printStackTrace()
                }

            })
        }).show()
    }
}