package com.example.abithshiksha.model.repo.get_suggestion_courses

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.suggestion_courses.GetSuggestionCoursesResponse
import com.example.abithshiksha.model.repo.Outcome

class GetSuggestionCoursesRepositoryImpl(private val context: Context): GetSuggestionCoursesRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun getSuggestionCourses(token: String): Outcome<GetSuggestionCoursesResponse> {
        val apiResponse = MutableLiveData<Outcome<GetSuggestionCoursesResponse>>()
        try {
            val response = apiService.getSuggestionCourse(token)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<GetSuggestionCoursesResponse>
    }
}