/**
 * description: 用于网络请求
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/5/3
 */

package com.example.zhihu.data.api

import com.example.zhihu.data.model.NewsResponse
import retrofit2.http.GET

interface ApiService {
    //get注解，表示GET请求
    @GET("api/4/news/latest")
    //suspend 协程网络请求，不能在主线程直接调用
    suspend fun getNews(): NewsResponse
}