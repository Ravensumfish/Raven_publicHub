package com.example.zhihu.main.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.zhihu.R
import com.example.zhihu.data.repository.NewsRepository
import com.example.zhihu.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
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
}