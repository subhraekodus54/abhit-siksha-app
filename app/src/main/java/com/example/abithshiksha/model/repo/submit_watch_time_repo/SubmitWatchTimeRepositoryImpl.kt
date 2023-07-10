package com.example.abithshiksha.model.repo.submit_watch_time_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.watch_time.SubmitWatchTimeRequest
import com.example.abithshiksha.model.pojo.watch_time.SubmitWatchTimeResponse
import com.example.abithshiksha.model.repo.Outcome

class SubmitWatchTimeRepositoryImpl(private val context: Context): SubmitWatchTimeRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun submitWatchTime(
        video_id: Int,
        video_start_time: String,
        video_end_time: String,
        token: String
    ): Outcome<SubmitWatchTimeResponse> {
        val apiResponse = MutableLiveData<Outcome<SubmitWatchTimeResponse>>()
        try {
            val response = apiService.submitWatchTime(SubmitWatchTimeRequest(video_id,video_start_time,video_end_time),token)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<SubmitWatchTimeResponse>
    }
}