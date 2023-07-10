package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.submit_watch_time_repo.SubmitWatchTimeRepository

class SubmitWatchTimeViewModel(private val mSubmitWatchTimeRepository: SubmitWatchTimeRepository): ViewModel() {

    fun submitWatchTime(
        video_id: Int,
        video_start_time: String,
        video_end_time: String,
        token: String
    ) = liveData {
        emit(mSubmitWatchTimeRepository.submitWatchTime(video_id,video_start_time,video_end_time,token))
    }
}