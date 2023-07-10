package com.example.abithshiksha.model.repo.get_topic_list_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.get_topic_list.GetTopicListResponse
import com.example.abithshiksha.model.repo.Outcome

class GetTopicListRepositoryImpl(private val context: Context): GetTopicListRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun getTopicList(
        token: String,
        lesson_id: Int
    ): Outcome<GetTopicListResponse> {
        val apiResponse = MutableLiveData<Outcome<GetTopicListResponse>>()
        try {
            val response = apiService.getTopicList(token,lesson_id)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<GetTopicListResponse>
    }
}