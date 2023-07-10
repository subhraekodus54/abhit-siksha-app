package com.example.abithshiksha.model.repo.get_topic_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.get_topics.GetTopicResponse
import com.example.abithshiksha.model.repo.Outcome

class GetTopicRepositoryImpl(private val context: Context): GetTopicRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun getTopic(
        token: String,
        lesson_id: Int,
        page: Int
    ): Outcome<GetTopicResponse> {
        val apiResponse = MutableLiveData<Outcome<GetTopicResponse>>()
        try {
            val response = apiService.getTopic(token,lesson_id, page)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<GetTopicResponse>
    }
}