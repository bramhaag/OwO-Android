package me.bramhaag.owoandroid.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.android.synthetic.main.upload_card.view.*
import me.bramhaag.owoandroid.data.UploadItem
import me.bramhaag.owoandroid.util.ByteUnit
import me.bramhaag.owouploader.R


class UploadItemAdapter(var items: List<UploadItem>, var context: Context) : RecyclerView.Adapter<UploadItemAdapter.UploadViewHolder>() {

    override fun onCreateViewHolder(parent: android.view.ViewGroup?, viewType: Int) = UploadViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.upload_card, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: UploadItemAdapter.UploadViewHolder, position: Int) =
            items[position].run {
                val mByteUnit = when (max) {
                    in ByteUnit.C_B ..ByteUnit.C_KB - 1 -> ByteUnit.BYTE
                    in ByteUnit.C_KB..ByteUnit.C_MB - 1 -> ByteUnit.KB
                    else -> ByteUnit.MB
                }

                holder.title.text = "Uploading $name"
                holder.progress.max = max.toInt()
                holder.progress.progress = progress.toInt()
                holder.progressText.text = "${mByteUnit.convert(progress, ByteUnit.BYTE)} ${mByteUnit.unit}/${mByteUnit.convert(max, ByteUnit.BYTE)} ${mByteUnit.unit}"
            }

    inner class UploadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var url: TextView
        var progress: ProgressBar
        var progressText: TextView

        init {
            itemView.tag = this
            title = itemView.upload_item_title
            url = itemView.upload_item_description
            progress = itemView.upload_item_progress
            progressText = itemView.upload_item_progress_text


            //itemView.setOnClickListener(ShortenHistoryItemListener(this))
            //itemView.setOnLongClickListener(ShortenHistoryItemListener(this))
        }
    }
}