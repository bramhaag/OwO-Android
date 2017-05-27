package me.bramhaag.owoandroid.listeners

import android.app.AlertDialog
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.view.View
import android.widget.Toast
import android.content.*
import me.bramhaag.owoandroid.adapters.UploadFileAdapter
import me.bramhaag.owoandroid.R


class CardListener(val file: UploadFileAdapter.FileViewHolder): View.OnClickListener, View.OnLongClickListener {

    override fun onClick(v: View) {
        if(file.url == null) Toast.makeText(v.context, v.context.getString(R.string.url_not_set), Toast.LENGTH_SHORT).show()
        else startActivity(v.context, Intent(Intent.ACTION_VIEW, Uri.parse(file.url.toString())), null)
    }

    override fun onLongClick(v: View): Boolean {
        AlertDialog.Builder(v.context)
                .setItems(arrayOf(v.context.getString(R.string.dialog_open_file), v.context.getString(R.string.dialog_copy_url)), { _, item ->
                    when(item) {
                        0 -> startActivity(v.context, Intent(Intent.ACTION_VIEW, Uri.parse(file.url.toString())), null)
                        1 -> {
                            val clipboard = v.context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.primaryClip = ClipData.newPlainText(file.url.toString(), file.url.toString())

                            Toast.makeText(v.context, v.context.getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show()
                        }
                    }
                }).show()

        return true
    }
}