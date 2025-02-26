package com.mu42.news.ui.theme

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mu42.news.R
import com.mu42.news.ui.NewsArticle

class NewsAdapter(private val articles: List<NewsArticle>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivArticleImage: ImageView = itemView.findViewById(R.id.ivArticleImage)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvAuthor: TextView = itemView.findViewById(R.id.tvAuthor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = articles[position]

        // Handle potential null values
        holder.tvTitle.text = article.title ?: "No title available"
        holder.tvAuthor.text = article.author?.takeIf { it.isNotBlank() }
            ?: "Unknown Author"

        // Load image only if URL is valid
        if (!article.urlToImage.isNullOrBlank()) {
            Glide.with(holder.itemView.context)
                .load(article.urlToImage)
                .placeholder(R.drawable.baseline_arrow_downward_24)
                .error(R.drawable.baseline_airplanemode_inactive_24)
                .into(holder.ivArticleImage)
        } else {
            Glide.with(holder.itemView.context)
                .load(R.drawable.baseline_airplanemode_inactive_24)
                .into(holder.ivArticleImage)
        }
    }

    override fun getItemCount() = articles.size
}