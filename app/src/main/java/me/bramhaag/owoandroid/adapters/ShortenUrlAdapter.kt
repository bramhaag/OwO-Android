package me.bramhaag.owoandroid.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import me.bramhaag.owoandroid.components.ShortenHistoryItem


class ShortenUrlAdapter(var urls: List<ShortenHistoryItem>, var context: Context) : RecyclerView.Adapter<ShortenUrlAdapter.UrlViewHolder>() {

    val dateFormat = java.text.SimpleDateFormat("dd-MM-yyyy HH:mm")

    override fun onCreateViewHolder(parent: android.view.ViewGroup?, viewType: Int) = UrlViewHolder(android.view.LayoutInflater.from(parent?.context).inflate(me.bramhaag.owoandroid.R.layout.upload_history_item, parent, false))

    override fun getItemCount() = urls.size

    //TODO use run? idk
    override fun onBindViewHolder(holder: me.bramhaag.owoandroid.adapters.ShortenUrlAdapter.UrlViewHolder, position: Int) =
            urls[position].run {
                holder.title.text = originalUrl.toString()
                holder.description.text = "$shortenedUrl - ${dateFormat.format(date)}"
            }

    inner class UrlViewHolder(itemView: android.view.View) : android.support.v7.widget.RecyclerView.ViewHolder(itemView) {
        var title: android.widget.TextView
        var description: android.widget.TextView

        init {
            itemView.tag = this
            title = itemView.findViewById(me.bramhaag.owoandroid.R.id.shorten_item_title) as android.widget.TextView
            description = itemView.findViewById(me.bramhaag.owoandroid.R.id.shorten_item_title) as android.widget.TextView
        }
    }
}