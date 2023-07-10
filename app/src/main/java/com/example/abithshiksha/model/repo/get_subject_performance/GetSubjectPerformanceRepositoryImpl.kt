package com.example.abithshiksha.model.repo.get_subject_performance

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.get_subject_performance.GetSubjectPerformanceResponse
import com.example.abithshiksha.model.repo.Outcome

class GetSubjectPerformanceRepositoryImpl(private val context: Context): GetSubjectPerformanceRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun getSubjectPerformance(
        token: String,
        id: Int
    ): Outcome<GetSubjectPerformanceResponse> {
        val apiResponse = MutableLiveData<Outcome<GetSubjectPerformanceResponse>>()
        try {
            val response = apiService.getSubjectPerformance(token,id)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<GetSubjectPerformanceResponse>
    }
}