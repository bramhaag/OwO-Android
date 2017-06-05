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
import me.bramhaag.owoandroid.adapters.ShortenUrlAdapter


class ShortenHistoryItemListener(val url: ShortenUrlAdapter.UrlViewHolder): View.OnClickListener, View.OnLongClickListener {

    override fun onClick(v: View) {
        startActivity(v.context, Intent(Intent.ACTION_VIEW, Uri.parse(url.shortenedUrl.toString())), null)
    }

    override fun onLongClick(v: View): Boolean {
        MaterialDialog.Builder(v.context)
                .items(v.context.getString(R.string.history_item_open),
                       v.context.getString(R.string.history_item_copy),
                       v.context.getString(R.string.history_item_open_shortened),
                       v.context.getString(R.string.history_item_copy_shortened))
                .itemsCallback { dialog, _, _, text ->
                    when(text) {
                        v.context.getString(R.string.history_item_open) -> {
                            startActivity(v.context, Intent(Intent.ACTION_VIEW, Uri.parse(url.originalUrl.toString())), null)
                        }
                        v.context.getString(R.string.history_item_copy) -> {
                            (v.context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip = ClipData.newPlainText(url.originalUrl.toString(), url.originalUrl.toString())
                            Toast.makeText(v.context, v.context.getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show()
                        }
                        v.context.getString(R.string.history_item_open_shortened) -> {
                            startActivity(v.context, Intent(Intent.ACTION_VIEW, Uri.parse(url.shortenedUrl.toString())), null)
                        }
                        v.context.getString(R.string.history_item_copy_shortened) -> {
                            (v.context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip = ClipData.newPlainText(url.shortenedUrl.toString(), url.shortenedUrl.toString())
                            Toast.makeText(v.context, v.context.getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show()
                        }
                    }

                    dialog.dismiss()
                }
                .autoDismiss(false)
                .show()

        return true
    }
}