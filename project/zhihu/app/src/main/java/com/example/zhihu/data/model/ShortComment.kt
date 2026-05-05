package com.example.zhihu.data.model

data class ShortComment(
    val author:String,
    val content:String,
    val avatar:String,
    val time: Long,
    val likes: Int
)