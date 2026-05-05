/**
 * description: 详情页
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/5/4
 */


package com.example.zhihu.main.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.DialogTitle
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.zhihu.R
import com.example.zhihu.data.repository.NewsRepository
import com.example.zhihu.databinding.ActivityDetailBinding
import com.example.zhihu.main.comment.CommentActivity

class DetailActivity : AppCompatActivity() {

    private val BASE_URL = "https://news-at.zhihu.com/"
    private lateinit var binding: ActivityDetailBinding
    private lateinit var adapter: PagerAdapter
    private val viewModel: DetailViewModel by viewModels()
    private var currentId: Int = -1
    private var idList: ArrayList<Int> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        initClick()
    }



    fun init() {
        currentId = intent.getIntExtra("id", -1)
        idList = intent.getIntegerArrayListExtra("id_list") ?: arrayListOf()
        viewModel.loadData(currentId)
        adapter = PagerAdapter()
        binding.vp2Detail.adapter = adapter
        binding.vp2Detail.offscreenPageLimit = idList.size

        Log.d("TAG","(listCount:)-->>${idList.size}")

        observeViewModel()
        loadDetailPage(idList, currentId)
    }

    private fun loadDetailPage(list: ArrayList<Int>, id: Int) {
        viewModel.loadData(id)
        Log.d("TAG","(id:)-->>${id}")

        //预加载相邻页面
        val currentPos = list.indexOf(id)
        if (currentPos > 0) {
            viewModel.loadData(list[currentPos - 1])
        }else{
            viewModel.loadData(list[list.size - 1])
        }

        if (currentPos < list.size - 1) {
            viewModel.loadData(list[currentPos + 1])
        }else{
            viewModel.loadData(list[0])
        }
    }

    private fun observeViewModel() {
        viewModel.detail.observe(this) { list ->
            adapter.submitList(list)
            Log.d("TAG","(listCount:submit)-->>${list.size}")
            binding.progressBar.visibility = View.GONE
        }

        viewModel.error.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initClick() {
        back()
        comment()
        share()
    }

    private fun share() {
        binding.shareDetail.setOnClickListener { v ->
            shareContent(this,"知乎日报",BASE_URL+"api/4/story/$currentId")
        }
    }

    private fun shareContent(context: Context,title: String,content:String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            //分享标题
            putExtra(Intent.EXTRA_SUBJECT,title)
            //分享内容
            putExtra(Intent.EXTRA_TEXT,"${title}\n$content")
        }
        context.startActivity(Intent.createChooser(shareIntent,"分享到"))
    }

    private fun comment() {
        binding.commentDetail.setOnClickListener { v ->
            val intent = Intent(this, CommentActivity::class.java)
            intent.putExtra("id",currentId)
            startActivity(intent)
        }
    }

    private fun back() {
        binding.backDetail.setOnClickListener { v ->
            finish()
        }
    }
}