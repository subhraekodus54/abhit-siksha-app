package com.example.abithshiksha.model.repo.upcomming_courses_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.upcomming_response.GetUpcommingCoursesResponse
import com.example.abithshiksha.model.repo.Outcome

class UpcommingCoursesRepositoryImpl(private val context: Context): UpcommingCoursesRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun getUpcommingCourses(token: String): Outcome<GetUpcommingCoursesResponse> {
        val apiResponse = MutableLiveData<Outcome<GetUpcommingCoursesResponse>>()
        try {
            val response = apiService.getUpcomingCourse(token)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<GetUpcommingCoursesResponse>
    }
}