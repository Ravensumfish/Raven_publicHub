package com.example.zhihu.main.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zhihu.data.model.NewsDetail
import com.example.zhihu.data.repository.NewsRepository
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
    private val _detail = MutableLiveData<List<NewsDetail>>()
    val detail : LiveData<List<NewsDetail>> = _detail

    private val detailMap = mutableMapOf<Int, NewsDetail>()
    private val repository = NewsRepository()

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadData(id : Int){
        if (detailMap.containsKey(id))return
        try {
            viewModelScope.launch {
                val response = repository.getNews(id)
                detailMap[id] = response
               _detail.value = detailMap.values.toList()
            }
        }catch (e: Exception){
            _error.value = e.message?:"请求失败"
            e.printStackTrace()
        }

    }
}