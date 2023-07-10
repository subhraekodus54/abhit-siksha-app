package com.example.abithshiksha.model.repo.get_mcq_result_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.mcq_result.GetMcqResultResponse
import com.example.abithshiksha.model.repo.Outcome

class GetMcqResultRepositoryImpl(private val context: Context): GetMcqResultRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun getMcqResult(token: String, id: Int): Outcome<GetMcqResultResponse> {
        val apiResponse = MutableLiveData<Outcome<GetMcqResultResponse>>()
        try {
            val response = apiService.getMcqResult(token,id)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<GetMcqResultResponse>
    }
}