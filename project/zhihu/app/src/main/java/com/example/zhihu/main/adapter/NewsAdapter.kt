package com.example.zhihu.main.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zhihu.main.model.BannerUIModel
import com.example.zhihu.main.model.NewsUIModel

class NewsAdapter(val list: List<data>): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    //用于刷新列表
    private val data = mutableListOf<NewsUIModel>()
    fun submitList(list: List<NewsUIModel>){
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewsAdapter.NewsViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: NewsAdapter.NewsViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }


}