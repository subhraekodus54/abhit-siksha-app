package com.example.abithshiksha.model.repo.get_lessons_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.get_lessons.GetLessonsResponse
import com.example.abithshiksha.model.repo.Outcome

class GetLessonsRepositoryImpl(private val context: Context): GetLessonsRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun getLessons(
        token: String,
        subject_id: Int,
        page: Int
    ): Outcome<GetLessonsResponse> {
        val apiResponse = MutableLiveData<Outcome<GetLessonsResponse>>()
        try {
            val response = apiService.getLessons(token,subject_id,page)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<GetLessonsResponse>
    }
}