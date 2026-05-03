package com.example.zhihu.main.pre

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zhihu.main.pre.adapter.BannerAdapter
import com.example.zhihu.main.pre.adapter.NewsAdapter
import com.example.zhihu.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    //创建binding
    private lateinit var binding: ActivityHomeBinding

    //viewModel
    private val viewModel: HomeViewModel by viewModels()

    //adapter
    private val newsAdapter: NewsAdapter = NewsAdapter()
    private val bannerAdapter: BannerAdapter = BannerAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //初始化
        init()
        initClick()


    }

    private fun initClick() {
        newsAdapter.onItemClick = { pos ->
            val item = newsAdapter.getItem(pos)
            Toast.makeText(this, "点击了news${item.id}", Toast.LENGTH_SHORT).show()

        }

        bannerAdapter.onItemClick = { pos ->
            val item = newsAdapter.getItem(pos)
            Toast.makeText(this, "点击了banner${item.id}", Toast.LENGTH_SHORT).show()

        }
    }

    fun init() {
        initRecyclerView()
        initVp2()
        //准备好“观察”，即接收数据的准备
        observeViewModel()
        //接收数据
        viewModel.loadHomeData()

    }

    private fun observeViewModel() {
        //当viewModel里的列表有变化，这里会自动刷新
        viewModel.newsList.observe(this) { list ->
            newsAdapter.submitList(list)
        }
        viewModel.bannerList.observe(this) { list ->
            bannerAdapter.submitList(list)
        }

        //错误监听
        viewModel.error.observe(this){message->
            Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
        }
    }

    private fun initRecyclerView() {
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = newsAdapter
    }

    private fun initVp2() {
        binding.vp2.adapter = bannerAdapter
    }
}