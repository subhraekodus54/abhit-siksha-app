package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.get_videos_repo.GetVideosRepository

class GetVideosViewModel (private val mGetVideosRepository: GetVideosRepository): ViewModel() {
    fun getVideos(
        token: String,
        lesson_id: Int,
        page: Int
    ) = liveData {
        emit(mGetVideosRepository.getVideos(token, lesson_id, page))
    }
}