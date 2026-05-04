/**
 * description: 构建请求体,执行apiService
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/5/3
 */

package com.example.zhihu.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://news-at.zhihu.com/"

    val apiService: ApiService by lazy{
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(ApiService::class.java)
    }
}