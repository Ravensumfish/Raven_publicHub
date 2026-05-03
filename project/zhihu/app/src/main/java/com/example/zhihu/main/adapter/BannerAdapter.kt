package com.example.zhihu.main.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zhihu.main.model.BannerUIModel

class BannerAdapter(): RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {
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
    ): BannerAdapter.BannerViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: BannerAdapter.BannerViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }


}