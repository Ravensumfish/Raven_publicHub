/**
 * description: 数据库，隔开网络层与viewModel
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/5/3
 */

package com.example.zhihu.data.repository

import com.example.zhihu.data.api.RetrofitClient
import com.example.zhihu.data.model.NewsResponse

class NewsRepository {
    private val api = RetrofitClient.apiService
    suspend fun getNews(): NewsResponse{
        return api.getNews()
    }
}