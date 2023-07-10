package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.get_topic_repo.GetTopicRepository

class GetTopicViewModel(private val mGetTopicRepository: GetTopicRepository): ViewModel() {
    fun getTopic(
        token: String,
        lesson_id: Int,
        page: Int
    ) = liveData {
        emit(mGetTopicRepository.getTopic(token, lesson_id, page))
    }
}