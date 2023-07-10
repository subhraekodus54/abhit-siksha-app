package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.get_topic_list_repo.GetTopicListRepository

class GetTopicListViewModel (private val mGetTopicListRepository: GetTopicListRepository): ViewModel() {
    fun getTopicList(
        token: String,
        lesson_id: Int
    ) = liveData {
        emit(mGetTopicListRepository.getTopicList(token,lesson_id))
    }
}