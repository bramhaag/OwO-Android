package me.bramhaag.owoandroid.listeners

import android.app.ProgressDialog
import android.content.*
import android.content.pm.PackageManager.PERMISSION_DENIED
import android.net.Uri
import android.preference.PreferenceManager
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat.requestPermissions
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.content.ContextCompat
import android.text.Html
import android.util.Log
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.activity_main.*
import me.bramhaag.owoandroid.R
import me.bramhaag.owoandroid.activities.MainActivity
import me.bramhaag.owoandroid.api.ProgressRequestBody
import me.bramhaag.owoandroid.util.ByteUnit
import me.bramhaag.owoandroid.util.Consumer
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL
import java.text.DecimalFormat
import java.util.*

class UploadButtonListener(val activity: MainActivity): View.OnClickListener {

    companion object {
        private val mDecimalFormat = DecimalFormat("#.##")
    }

    private val GALLERY_REQUEST = 0
    private val mUploadQueue = LinkedList<Uri>()
    lateinit var dialog: ProgressDialog

    init {
        activity.resultConsumerMap.put(GALLERY_REQUEST, object : Consumer<Intent?> {
            override fun accept(t: Intent?) {
                dialog = ProgressDialog(activity).apply {
                    setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                    setCancelable(false)
                    setCanceledOnTouchOutside(false)
                }

                if(t?.data != null) mUploadQueue.add(t.data)
                else if(t?.clipData != null) (0..t.clipData.itemCount - 1).mapTo(mUploadQueue) { t.clipData.getItemAt(it).uri }

                upload(mUploadQueue.first, 0, mUploadQueue.count())
            }
        })
    }

    override fun onClick(v: View) {
        requestPermissions(activity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        if(ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PERMISSION_DENIED) return

        val intent = Intent(Intent.ACTION_GET_CONTENT)
                    .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                    .setType("*/*")
        startActivityForResult(activity, intent, GALLERY_REQUEST, null)
    }

    fun upload(uri: Uri, index: Int, total: Int) {
        dialog.setTitle(activity.getString(R.string.dialog_upload_title, index + 1, total))

        val requestFile = ProgressRequestBody(uri, dialog.context, object: ProgressRequestBody.ProgressConsumer {
            override fun accept(progress: Double, max: Double, byteUnit: ByteUnit) {
                dialog.progress = progress.toInt()
                dialog.setProgressNumberFormat("${mDecimalFormat.format(byteUnit.convert(progress, ByteUnit.BYTE))} ${byteUnit.unit} / ${mDecimalFormat.format(byteUnit.convert(max, ByteUnit.BYTE))} ${byteUnit.unit}")
            }
        })

        dialog.max = requestFile.size.toInt()

        val requestPart = MultipartBody.Part.createFormData("files[]", requestFile.name, requestFile)
        val uploadCall = MainActivity.owo.service.upload(requestPart)

        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, activity.getString(android.R.string.cancel), { _, _ ->
            uploadCall.cancel()
            mUploadQueue.clear()
            dialog.dismiss()
        })

        dialog.show()

        uploadCall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (call.isCanceled || !response.isSuccessful) {
                    dialog.dismiss()

                    @Suppress("DEPRECATION")
                    MaterialDialog.Builder(activity)
                            .title(R.string.error_title)
                            .content(
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                                        Html.fromHtml(activity.getString(R.string.upload_error_message, 200, response.code(), response.errorBody()?.string()), Html.FROM_HTML_MODE_LEGACY)
                                    else
                                        Html.fromHtml(activity.getString(R.string.upload_error_message, 200, response.code(), response.errorBody()?.string()))
                            )
                            .positiveText(R.string.ok)
                            .show()
                    return
                }

                mUploadQueue.removeFirst()
                val obj = JSONObject(response.body()?.string()).getJSONArray("files").getJSONObject(0)
                val url = URL("${PreferenceManager.getDefaultSharedPreferences(activity).getString("pref_destination", "https://owo.whats-th.is")}/${obj.getString("url")}")
                MainActivity.mRecycleViewManager.addFile(obj.getString("name"), url, Date())

                if (mUploadQueue.isNotEmpty()) {
                    upload(mUploadQueue.first, index + 1, total)
                    return
                }

                dialog.dismiss()
                if (total > 1) {
                    Snackbar.make(activity.main_scroll_view, activity.getString(R.string.snackbar_upload_multi_content, total), Snackbar.LENGTH_LONG).show()
                } else {
                    Snackbar.make(activity.main_scroll_view, activity.getString(R.string.snackbar_upload_single_content), Snackbar.LENGTH_LONG)
                            .setAction(R.string.dialog_open_file, { ContextCompat.startActivity(activity, Intent(Intent.ACTION_VIEW, Uri.parse(url.toString())), null) })
                            .show()

                    (activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip = ClipData.newPlainText(url.toString(), url.toString())
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Upload error:", t.message)
                t.printStackTrace()
            }
        })
    }
}