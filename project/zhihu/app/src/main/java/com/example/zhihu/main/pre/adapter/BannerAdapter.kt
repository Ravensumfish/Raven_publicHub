/**
 * description: banner适配器
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/5/3
 */

package com.example.zhihu.main.pre.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.zhihu.R
import com.example.zhihu.main.pre.model.BannerUIModel

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

        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .centerCrop()
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun getItem(position: Int): BannerUIModel{
        return data[position]
    }

    fun getIdList(): ArrayList<Int>{
        return ArrayList(data.map { it.id })
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