/**
 * description: 页面vp2适配
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/5/4
 */


package com.example.zhihu.main.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.zhihu.R
import com.example.zhihu.data.api.ApiService
import com.example.zhihu.data.api.RetrofitClient
import com.example.zhihu.data.model.NewsDetail
import com.example.zhihu.data.repository.NewsRepository
import com.example.zhihu.main.pre.model.NewsUIModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PagerAdapter : RecyclerView.Adapter<PagerAdapter.PagerViewHolder>() {

    private val data = mutableListOf<NewsDetail>()
    fun submitList(list: List<NewsDetail>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    fun currentList() : List<NewsDetail> {
        return data
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PagerAdapter.PagerViewHolder {
        val view  = LayoutInflater.from(parent.context)
            .inflate(R.layout.page_detail,parent,false)
        return PagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PagerAdapter.PagerViewHolder, position: Int) {
        val item = data[position]
        Glide.with(holder.itemView.context)
            .load(item.image)
            .into(holder.image)

        holder.title.text = item.title

        val cssUrl = item.css.firstOrNull()?:""
        val html = """
            <!DOCTYPE html>
        <html>
        <head>
            <meta charset="utf-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <link rel="stylesheet" href="$cssUrl" type="text/css"/>
            <style>
                /* 基础样式重置 */
                * { margin: 0; padding: 0; box-sizing: border-box; }
                
                body {
                    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
                    font-size: 16px;
                    line-height: 1.8;
                    color: #333;
                    padding: 16px;
                    background: #fff;
                }
               
                /* 正文图片 */
                img {
                    max-width: 100%;
                    height: auto;
                    display: block;
                    margin: 16px auto;
                }
                
                /* 段落 */
                p {
                    margin-bottom: 16px;
                    text-align: justify;
                }
                
                /* 引用块 */
                blockquote {
                    border-left: 4px solid #ddd;
                    padding-left: 16px;
                    margin: 16px 0;
                    color: #666;
                }
                
                /* 链接 */
                a {
                    color: #0066cc;
                    text-decoration: none;
                }
                
                /* 作者信息 */
                .meta {
                    font-size: 14px;
                    color: #999;
                    margin-bottom: 20px;
                    padding-bottom: 16px;
                    border-bottom: 1px solid #eee;
                }
            </style>
        </head>
        <body>
            ${item.body}
        </body>
        </html>
        """.trimIndent()
        holder.content.loadDataWithBaseURL(null,html,"text/html", "utf-8", null)

    }

    override fun getItemCount(): Int {
        return data.size
    }

    class PagerViewHolder(item : View) : RecyclerView.ViewHolder(item){
        val content : WebView = item.findViewById(R.id.webView)
        val image : ImageView = item.findViewById(R.id.image_detail)
        val title : TextView = item.findViewById(R.id.title_detail)

    }
}