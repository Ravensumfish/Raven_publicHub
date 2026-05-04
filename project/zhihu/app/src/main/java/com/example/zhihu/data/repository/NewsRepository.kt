/**
 * description: 数据库（中转站），隔开网络层与viewModel，管理数据来源，转发网络请求
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/5/3
 */

package com.example.zhihu.data.repository

import com.example.zhihu.data.api.RetrofitClient
import com.example.zhihu.data.model.NewsDetail
import com.example.zhihu.data.model.NewsResponse

class NewsRepository {
    private val api = RetrofitClient.apiService
    suspend fun getNews(): NewsResponse{
        return api.getNews()
    }
    suspend fun getNews(id:Int): NewsDetail{
        return api.getNews(id)
    }

    suspend fun getBeforeNews(date:String): NewsResponse{
        return api.getBeforeNews(date)
    }
}