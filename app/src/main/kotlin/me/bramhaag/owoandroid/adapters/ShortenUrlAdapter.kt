package me.bramhaag.owoandroid.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import me.bramhaag.owoandroid.R
import me.bramhaag.owoandroid.components.ShortenHistoryItem
import me.bramhaag.owoandroid.listeners.ShortenHistoryItemListener
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class ShortenUrlAdapter(var urls: List<ShortenHistoryItem>, var context: Context) : RecyclerView.Adapter<ShortenUrlAdapter.UrlViewHolder>() {

    val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())

    override fun onCreateViewHolder(parent: android.view.ViewGroup?, viewType: Int) = UrlViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.shorten_history_item, parent, false))

    override fun getItemCount() = urls.size

    override fun onBindViewHolder(holder: ShortenUrlAdapter.UrlViewHolder, position: Int) =
            urls[position].run {
                holder.originalUrl.text = originalUrl.toString()
                holder.description.text = "$shortenedUrl - ${dateFormat.format(date)}"

                holder.shortenedUrl = shortenedUrl
            }

    inner class UrlViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var originalUrl: TextView
        var description: TextView

        lateinit var shortenedUrl: URL

        init {
            itemView.tag = this
            originalUrl = itemView.findViewById(R.id.shorten_item_original_url) as TextView
            description = itemView.findViewById(R.id.shorten_item_shortened_url) as TextView

            itemView.setOnClickListener(ShortenHistoryItemListener(this))
            itemView.setOnLongClickListener(ShortenHistoryItemListener(this))
        }


    }
}