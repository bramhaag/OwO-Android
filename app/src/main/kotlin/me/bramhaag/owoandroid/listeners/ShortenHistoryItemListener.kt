package me.bramhaag.owoandroid.listeners

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.view.View
import android.widget.Toast
import me.bramhaag.owoandroid.R
import me.bramhaag.owoandroid.adapters.ShortenUrlAdapter


class ShortenHistoryItemListener(val url: ShortenUrlAdapter.UrlViewHolder): View.OnClickListener, View.OnLongClickListener {

    override fun onClick(v: View) {
        startActivity(v.context, Intent(Intent.ACTION_VIEW, Uri.parse(url.shortenedUrl.toString())), null)
    }

    override fun onLongClick(v: View): Boolean {
        AlertDialog.Builder(v.context)
                //TODO String
                .setItems(arrayOf("Open URL", "Copy URL", "Open shortened URL", "Copy shortened URL"), { _, item ->
                    when(item) {
                        0 -> startActivity(v.context, Intent(Intent.ACTION_VIEW, Uri.parse(url.originalUrl.toString())), null)
                        1 -> {
                            (v.context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip = ClipData.newPlainText(url.originalUrl.toString(), url.originalUrl.toString())
                            Toast.makeText(v.context, v.context.getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show()
                        }
                        2 -> startActivity(v.context, Intent(Intent.ACTION_VIEW, Uri.parse(url.shortenedUrl.toString())), null)
                        3 -> {
                            (v.context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip = ClipData.newPlainText(url.shortenedUrl.toString(), url.shortenedUrl.toString())
                            Toast.makeText(v.context, v.context.getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show()
                        }
                    }
                }).show()

        return true
    }
}