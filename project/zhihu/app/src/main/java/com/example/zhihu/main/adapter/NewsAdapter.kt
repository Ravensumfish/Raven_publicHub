package com.example.zhihu.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zhihu.R
import com.example.zhihu.main.model.BannerUIModel
import com.example.zhihu.main.model.NewsUIModel

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    //用于刷新列表
    private val data = mutableListOf<NewsUIModel>()
    fun submitList(list: List<NewsUIModel>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)

        return NewsViewHolder(view)

    }

    override fun onBindViewHolder(holder: NewsAdapter.NewsViewHolder, position: Int) {
        val item = data[position]
        holder.title.text = item.title
        holder.author.text = item.author

    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun getItem(position: Int): NewsUIModel {
        return data[position]
    }

    var onItemClick: ((Int) -> Unit)? = null

    inner class NewsViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val title: TextView = item.findViewById(R.id.title_news)
        val author: TextView = item.findViewById(R.id.author_name_news)
        val image: ImageView = item.findViewById(R.id.image_news)

        init {
            item.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    onItemClick?.invoke(pos)
                }
            }
        }

    }

}