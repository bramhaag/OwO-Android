package me.bramhaag.owoandroid.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.upload_history_item.view.*
import me.bramhaag.owoandroid.R
import me.bramhaag.owoandroid.components.UploadHistoryItem
import me.bramhaag.owoandroid.listeners.UploadHistoryItemListener
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class UploadFileAdapter(var files: List<UploadHistoryItem>, var context: Context) : RecyclerView.Adapter<UploadFileAdapter.FileViewHolder>() {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = FileViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.upload_history_item, parent, false))

    override fun getItemCount() = files.size

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
         files[position].run {
             holder.title.text = name
             holder.description.text = "${url.host} - ${dateFormat.format(date)}"
             holder.url = url
             Glide.with(context)
                     .load(url.toString())
                     .centerCrop()
                     .placeholder(R.drawable.not_found)
                     .into(holder.image)
         }
    }

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var description: TextView
        var image: ImageView
        lateinit var url: URL

        init {
            itemView.tag = this
            title = itemView.upload_item_title
            description = itemView.upload_item_description
            image = itemView.upload_item_image

            itemView.setOnClickListener(UploadHistoryItemListener(this))
            itemView.setOnLongClickListener(UploadHistoryItemListener(this))
        }
    }
}