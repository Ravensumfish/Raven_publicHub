package com.example.zhihu.main.detail

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.zhihu.R
import com.example.zhihu.data.repository.NewsRepository
import com.example.zhihu.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
      private lateinit var binding : ActivityDetailBinding
      private lateinit var adapter: PagerAdapter
      private val viewModel : DetailViewModel by viewModels()
      private var id : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

    }

    fun init(){
        id = intent.getIntExtra("id",-1)
        viewModel.loadData(id)
        adapter = PagerAdapter()
        binding.vp2Detail.adapter = adapter

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.detail.observe(this){detail ->

        }
    }
}