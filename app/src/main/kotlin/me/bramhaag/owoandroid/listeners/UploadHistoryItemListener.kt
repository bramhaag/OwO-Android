package me.bramhaag.owoandroid.listeners

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.view.View
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import me.bramhaag.owoandroid.R
import me.bramhaag.owoandroid.adapters.UploadFileAdapter


class UploadHistoryItemListener(val file: UploadFileAdapter.FileViewHolder): View.OnClickListener, View.OnLongClickListener {

    override fun onClick(v: View) = startActivity(v.context, Intent(Intent.ACTION_VIEW, Uri.parse(file.url.toString())), null)

    override fun onLongClick(v: View): Boolean {
        MaterialDialog.Builder(v.context)
                .items(v.context.getString(R.string.history_item_open), v.context.getString(R.string.history_item_copy))
                .itemsCallback { _, _, _, text ->
                    when (text) {
                        v.context.getString(R.string.history_item_open) -> {
                            startActivity(v.context, Intent(Intent.ACTION_VIEW, Uri.parse(file.url.toString())), null)
                        }
                        v.context.getString(R.string.history_item_copy) -> {
                            (v.context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip = ClipData.newPlainText(file.url.toString(), file.url.toString())
                            Toast.makeText(v.context, v.context.getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .show()

        return true
    }
}