package com.example.abithshiksha.model.repo.get_all_performance_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.get_all_performance.GetAllPerformanceResponse
import com.example.abithshiksha.model.repo.Outcome

class GetAllPerformanceRepositoryImpl(private val context: Context): GetAllPerformanceRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun getAllPerformance(token: String): Outcome<GetAllPerformanceResponse> {
        val apiResponse = MutableLiveData<Outcome<GetAllPerformanceResponse>>()
        try {
            val response = apiService.getAllPerformance(token)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }
        return apiResponse.value as Outcome<GetAllPerformanceResponse>
    }
}