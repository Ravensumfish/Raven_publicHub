package com.example.zhihu.main.pre

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.zhihu.databinding.ActivityHomeBinding
import com.example.zhihu.main.pre.adapter.BannerAdapter
import com.example.zhihu.main.pre.adapter.NewsAdapter
import kotlinx.coroutines.Runnable

class HomeActivity : AppCompatActivity() {
    //创建binding
    private lateinit var binding: ActivityHomeBinding

    //viewModel
    private val viewModel: HomeViewModel by viewModels()

    //adapter
    private val newsAdapter: NewsAdapter = NewsAdapter()
    private val bannerAdapter: BannerAdapter = BannerAdapter()

    //handler:子线程，用于自动轮播
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //初始化
        init()
        initClick()
        initEvent()

    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable,3000)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable )
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable )
    }

    private fun initEvent() {
        autoPlay()
    }

    private fun autoPlay() {
        runnable = Runnable {
            val count = bannerAdapter.itemCount
            Log.d("TAG", "(itemCount:autoPlay)-->>$count")
            if (count > 0) {
                binding.vp2.currentItem = (binding.vp2.currentItem + 1) % count
            }
            handler.postDelayed(runnable, 3000)

            //手动滑动banner时重置计时
            binding.vp2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    handler.removeCallbacks(runnable)
                    handler.postDelayed(runnable,3000)
                }
            })

        }
    }

    private fun initClick() {
        newsAdapter.onItemClick = { pos ->
            val item = newsAdapter.getItem(pos)
            Toast.makeText(this, "点击了news${item.id}", Toast.LENGTH_SHORT).show()

        }

        bannerAdapter.onItemClick = { pos ->
            val item = bannerAdapter.getItem(pos)
            Toast.makeText(this, "点击了banner${item.id}", Toast.LENGTH_SHORT).show()
            //网络请求异步，若要打印相关信息应该在这里
            Log.d("TAG", "(itemCount:)-->>${bannerAdapter.itemCount}")
        }
    }

    fun init() {
        initRecyclerView()
        initVp2()
        //准备好观察（监听），即接收数据的准备
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
        viewModel.error.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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

