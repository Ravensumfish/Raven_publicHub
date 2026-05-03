package com.example.zhihu.main.pre.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zhihu.R
import com.example.zhihu.main.model.BannerUIModel
import com.example.zhihu.main.model.NewsUIModel

class BannerAdapter: RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {
    //用于刷新banner
    private val data = mutableListOf<BannerUIModel>()
    fun submitList(list: List<BannerUIModel>){
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BannerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_banner,parent,false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val item = data[position]
        holder.title.text = item.title
        holder.author.text = item.author
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun getItem(position: Int): BannerUIModel{
        return data[position]
    }

    var onItemClick :((Int)-> Unit)? = null
    inner class BannerViewHolder(item: View): RecyclerView.ViewHolder(item){
        val title: TextView = item.findViewById(R.id.title_banner)
        val author: TextView = item.findViewById(R.id.author_name_banner)
        val image: ImageView = item.findViewById(R.id.image_banner)

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