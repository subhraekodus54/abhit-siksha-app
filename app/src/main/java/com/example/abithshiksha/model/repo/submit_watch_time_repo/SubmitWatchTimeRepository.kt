package com.example.abithshiksha.model.repo.submit_watch_time_repo

import com.example.abithshiksha.model.pojo.watch_time.SubmitWatchTimeResponse
import com.example.abithshiksha.model.repo.Outcome

interface SubmitWatchTimeRepository {
    suspend fun submitWatchTime(
        video_id: Int,
        video_start_time: String,
        video_end_time: String,
        token: String
    ): Outcome<SubmitWatchTimeResponse>
}