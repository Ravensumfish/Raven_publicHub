package com.example.zhihu.main.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.recyclerview.widget.RecyclerView
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
        val cssUrl = item.css.firstOrNull()?:""
        val html = """
            <html>
            <head>
                <link rel="stylesheet" href="${cssUrl}"/>
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

    }
}