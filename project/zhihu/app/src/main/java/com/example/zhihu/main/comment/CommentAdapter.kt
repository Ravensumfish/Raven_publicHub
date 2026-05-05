package com.example.zhihu.main.comment

import android.icu.util.TimeZone
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.zhihu.R
import com.example.zhihu.data.model.ShortComment
import com.example.zhihu.main.pre.adapter.NewsAdapter
import com.example.zhihu.main.pre.model.NewsUIModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.time.Instant

class CommentAdapter: RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    //用于刷新列表
    private val data = mutableListOf<ShortComment>()
    fun submitList(list: List<ShortComment>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)

        return CommentViewHolder(view)

    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val item = data[position]
        holder.content.text = item.content
        holder.author.text = item.author
        holder.time.text = formatTime(item.time)

        Glide.with(holder.itemView.context)
            .load(item.avatar)
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .centerCrop()
            .into(holder.avatar)

    }

    fun formatTime(time : Long):String{
        val t = time * 1000L
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.getDefault())
        return sdf.format(Date(t))
    }


    override fun getItemCount(): Int {
        return data.size
    }



    class CommentViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val content: TextView = item.findViewById(R.id.content_comment)
        val author: TextView = item.findViewById(R.id.author_comment)
        val avatar: ImageView = item.findViewById(R.id.avatar_comment)
        val time: TextView = item.findViewById(R.id.time_comment)



    }
}