/**
 * description: commentActivity
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/5/5
 */


package com.example.zhihu.main.comment

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zhihu.R
import com.example.zhihu.databinding.ActivityCommentBinding

class CommentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCommentBinding
    private val viewModel : CommentViewModel by viewModels()
    private var adapter: CommentAdapter = CommentAdapter()
    private var id : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarComment)
        init()
    }

    fun init(){
        id = intent.getIntExtra("id",-1)
        initRv()
        initActionBar()
        observeViewModel()
        //接收数据
        viewModel.loadComment(id)
    }

    private fun initActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        binding.toolbarComment.setNavigationOnClickListener {
            finish()
        }
    }

    private fun observeViewModel() {
        viewModel.commentList.observe(this){ list ->
            adapter.submitList(list)
            val text = list.size.toString() + "条短评"
            binding.commentCount.text =  text
            Log.d("TAG","(submit:comments)-->>获取完毕");
        }
    }

    private fun initRv() {
        binding.rvComment.layoutManager = LinearLayoutManager(this)
        binding.rvComment.adapter = adapter
    }
}