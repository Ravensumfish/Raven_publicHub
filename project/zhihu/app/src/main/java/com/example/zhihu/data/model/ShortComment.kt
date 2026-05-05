/**
 * description: 实体类具体短评
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/5/5
 */

package com.example.zhihu.data.model

data class ShortComment(
    val author:String,
    val content:String,
    val avatar:String,
    val time: Long,
    val likes: Int
)