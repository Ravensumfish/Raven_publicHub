/**
 * description: 实体类，请求体，与Json一一对应
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/5/3
 */

package com.example.zhihu.data.model

data class NewsResponse(
    val date:String,
    val stories : List<Story>,
    val top_stories : List<TopStory>)