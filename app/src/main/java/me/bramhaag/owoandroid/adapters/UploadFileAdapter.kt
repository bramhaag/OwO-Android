package me.bramhaag.owoandroid.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import me.bramhaag.owoandroid.R
import me.bramhaag.owoandroid.components.UploadHistoryItem
import java.net.URL
import me.bramhaag.owoandroid.listeners.CardListener
import java.text.SimpleDateFormat


class UploadFileAdapter(var files: List<UploadHistoryItem>, var context: Context) : RecyclerView.Adapter<UploadFileAdapter.FileViewHolder>() {

    val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm")

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = FileViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.upload_history_item, parent, false))

    override fun getItemCount() = files.size

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val file = files[position]

        holder.title.text = file.name
        holder.description.text = "${file.url?.host} - ${dateFormat.format(file.date)}"
        holder.url = file.url
        Glide.with(context)
                .load(file.url.toString())
                .fitCenter()
                .placeholder(R.drawable.not_found)
                .into(holder.image)

    }

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var description: TextView
        var image: ImageView
        var url: URL? = null

        init {
            itemView.tag = this
            title = itemView.findViewById(R.id.upload_item_title) as TextView
            description = itemView.findViewById(R.id.upload_item_description) as TextView
            image = itemView.findViewById(R.id.upload_item_image) as ImageView

            itemView.setOnClickListener(CardListener(this))
            itemView.setOnLongClickListener(CardListener(this))
        }
    }
}