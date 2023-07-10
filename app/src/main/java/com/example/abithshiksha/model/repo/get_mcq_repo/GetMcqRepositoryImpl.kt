package com.example.abithshiksha.model.repo.get_mcq_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.get_mcq.GetMcqResponse
import com.example.abithshiksha.model.repo.Outcome

class GetMcqRepositoryImpl(private val context: Context): GetMcqRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun getMcq(token: String, set_id: Int, page: Int): Outcome<GetMcqResponse> {
        val apiResponse = MutableLiveData<Outcome<GetMcqResponse>>()
        try {
            val response = apiService.getMcq(token,set_id,page)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<GetMcqResponse>
    }
}