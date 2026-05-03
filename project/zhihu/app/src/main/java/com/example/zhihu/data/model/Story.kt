/**
 * description: 实体类，请求体相关news
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/5/3
 */

package com.example.zhihu.data.model

data class Story(
    val id: Int,
    val title:String,
    val hint :String,
    val url:String,
    val images:List<String>?
)