/**
 * description: 用于网络请求，定义接口
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/5/3
 */

package com.example.zhihu.data.api

import com.example.zhihu.data.model.NewsDetail
import com.example.zhihu.data.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    //get注解，表示GET请求
    @GET("api/4/news/latest")
    //suspend 协程网络请求，不能在主线程直接调用
    suspend fun getNews(): NewsResponse
    @GET("api/4/news/{id}")
    //suspend 协程网络请求，不能在主线程直接调用
    suspend fun getNews(@Path("id") id:Int): NewsDetail

    @GET("api/4/news/before/{date}")
    suspend fun getBeforeNews(@Path("date") date:String): NewsResponse
}