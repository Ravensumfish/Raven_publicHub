
/**
 * description: comment数据管理
 * author:Manticore
 * email:3100776336@qq.com
 * date:2026/5/5
 */

package com.example.zhihu.main.comment
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zhihu.data.model.ShortComment
import com.example.zhihu.data.repository.NewsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CommentViewModel : ViewModel() {

    private val _commentList = MutableLiveData<List<ShortComment>>()
    val commentList: LiveData<List<ShortComment>> = _commentList

    private val repository = NewsRepository()

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error


    fun loadComment(id :Int) {
        viewModelScope.launch {
            try {
                //得到请求，并与实体类的成员变量一一对应
                val response = repository.getShortComments(id)

                val commentList = response.comments.map { c->
                    ShortComment(
                        author = c.author,
                        content = c.content,
                        avatar = c.avatar,
                        time = c.time,
                        likes = c.likes
                    )
                }
                Log.d("NET", "(ViewModel:请求comments)-->>成功--$response")

                _commentList.value = commentList
            }catch (e: Exception){
                _error.value = e.message?:"请求失败"
                Log.d("NET","(ViewModel:请求comments)-->>失败")
            }
        }
    }



}