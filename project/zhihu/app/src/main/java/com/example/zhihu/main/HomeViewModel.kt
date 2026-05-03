package com.example.zhihu.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zhihu.main.model.BannerUIModel
import com.example.zhihu.main.model.NewsUIModel

class HomeViewModel : ViewModel() {
    //使viewModel持有数据，并通过LiveData通知给UI层
    //若 val bannerList = listOf<bannerUIModel>  这是死数据，无法被监听变化使UI同步更新
    //分两个变量_..List和..List，做到一个可修改，一个只读，区分开来保证viewModel的职责,也避免activity中篡改了数据
    //总而言之 viewModel:数据管理
    //LiveData:数据通知
    //Activity:只读数据
    private val _bannerList = MutableLiveData<List<BannerUIModel>>()
    val bannerList : LiveData<List<BannerUIModel>> = _bannerList

    private val _newsList = MutableLiveData<List<NewsUIModel>>()
    val newsList : LiveData<List<NewsUIModel>> = _newsList

    fun loadHomeData(){
        _bannerList.value = listOf(
            BannerUIModel(1,"小明","你好好好好好好好好好好好好好好",""),
            BannerUIModel(2,"小明","嗯嗯呢嗯呢呢咩咩咩咩咩咩咩咩吗买买买",""),
            BannerUIModel(3,"小明","你好","")

        )

        _newsList.value = listOf(
            NewsUIModel(1,"小明","你好好好好好好好好好好好好好好",""),
            NewsUIModel(2,"小明","嗯嗯呢嗯呢呢咩咩咩咩咩咩咩咩吗买买买",""),
            NewsUIModel(3,"小明","你好","")
        )
    }
}