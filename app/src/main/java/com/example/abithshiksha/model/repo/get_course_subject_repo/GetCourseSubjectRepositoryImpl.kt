package com.example.abithshiksha.model.repo.get_course_subject_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.get_course_subject.GetCourseSubjectResponse
import com.example.abithshiksha.model.repo.Outcome

class GetCourseSubjectRepositoryImpl(private val context: Context): GetCourseSubjectRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun getSubjects(
        token: String,
        cart_id: Int
    ): Outcome<GetCourseSubjectResponse> {
        val apiResponse = MutableLiveData<Outcome<GetCourseSubjectResponse>>()
        try {
            val response = apiService.getCourseSubject(token,cart_id)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<GetCourseSubjectResponse>
    }
}