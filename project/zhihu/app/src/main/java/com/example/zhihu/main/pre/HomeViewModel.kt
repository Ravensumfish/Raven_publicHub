/**
 * description: 管理数据
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/5/3
 */

package com.example.zhihu.main.pre

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zhihu.data.repository.NewsRepository
import com.example.zhihu.main.pre.model.BannerUIModel
import com.example.zhihu.main.pre.model.NewsUIModel
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    //使viewModel持有数据，并通过LiveData通知给UI层
    //若 val bannerList = listOf<bannerUIModel>  这是死数据，无法被监听变化使UI同步更新
    //分两个变量_..List和..List，做到一个可修改，一个只读，区分开来保证viewModel的职责,也避免activity中篡改了数据
    //总而言之 viewModel:数据管理
    //LiveData:数据通知
    //Activity:只读数据
    private val _bannerList = MutableLiveData<List<BannerUIModel>>()
    val bannerList: LiveData<List<BannerUIModel>> = _bannerList

    private val _newsList = MutableLiveData<List<NewsUIModel>>()
    val newsList: LiveData<List<NewsUIModel>> = _newsList

    private val repository = NewsRepository()

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private var currentDate:String? = null
    private var isLoading = false

    fun loadHomeData() {
        viewModelScope.launch {
            loadBanner()
            loadNews()
        }
    }

    fun refresh(){
        currentDate = null
        loadHomeData()
    }

    private suspend fun loadNews() {
        try {
            //得到请求，并与实体类的成员变量一一对应
            val response = repository.getNews()
            currentDate = response.date
            val newsUIList = response.stories.map { story ->
                NewsUIModel(
                    id = story.id,
                    author = story.hint,
                    title = story.title,
                    imageUrl = story.images?.firstOrNull() ?: ""
                )
            }
            Log.d("NET", "(ViewModel:请求news)-->>成功--$response")

            _newsList.value = newsUIList
        }catch (e: Exception){
            _error.value = e.message?:"请求失败"
            Log.d("NET","(ViewModel:请求news)-->>失败")
        }
    }

    private suspend fun loadBanner() {
        try {
            //得到请求，并与实体类的成员变量一一对应
            val response = repository.getNews()
            val bannerUIList = response.top_stories.map { story ->
                BannerUIModel(
                    id = story.id,
                    author = story.hint,
                    title = story.title,
                    imageUrl = story.image
                )
            }
            Log.d("NET", "(ViewModel:请求banner)-->>成功--$response")
            _bannerList.value = bannerUIList
        }catch (e: Exception){
            _error.value = e.message?:"请求失败"
            Log.d("NET","(ViewModel:请求banner)-->>失败")
        }
    }

    fun loadMore(){
        if (isLoading)return

        isLoading = true
        val date = currentDate?:return

        viewModelScope.launch {
            try {
                val response = repository.getBeforeNews(date)
                currentDate = response.date

                val moreList = response.stories.map {story->
                    NewsUIModel(
                        id = story.id,
                        author = story.hint,
                        title = story.title,
                        imageUrl = story.images?.firstOrNull() ?: ""
                    )
                }

                val currentList = _newsList.value.orEmpty()
                _newsList.value = currentList + moreList
            }catch (e: Exception){
                _error.value = e.message?:"请求失败"
                Log.d("NET","(ViewModel:请求more)-->>失败")
            }finally {
                isLoading = false
            }

        }
    }
}