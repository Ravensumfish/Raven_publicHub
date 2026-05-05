/**
 * description: 实体类具体news
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/5/4
 */


package com.example.zhihu.data.model

data class NewsDetail(
    val body:String,
    val css: List<String>,
    val image : String?,
    val title : String,
)