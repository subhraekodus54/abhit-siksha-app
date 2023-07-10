package com.example.abithshiksha.model.repo.get_videos_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.get_videos.GetVideosResponse
import com.example.abithshiksha.model.repo.Outcome

class GetVideosRepositoryImpl(private val context: Context): GetVideosRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun getVideos(token: String, lesson_id: Int, page: Int): Outcome<GetVideosResponse> {
        val apiResponse = MutableLiveData<Outcome<GetVideosResponse>>()
        try {
            val response = apiService.getVideos(token,lesson_id,page)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<GetVideosResponse>
    }
}