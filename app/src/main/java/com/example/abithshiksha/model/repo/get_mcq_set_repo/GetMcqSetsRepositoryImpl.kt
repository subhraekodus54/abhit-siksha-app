package com.example.abithshiksha.model.repo.get_mcq_set_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.get_mcq_sets.GetMcqSetsResponse
import com.example.abithshiksha.model.repo.Outcome

class GetMcqSetsRepositoryImpl(private val context: Context): GetMcqSetsRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun getMcqSets(token: String, lesson_id: Int): Outcome<GetMcqSetsResponse> {
        val apiResponse = MutableLiveData<Outcome<GetMcqSetsResponse>>()
        try {
            val response = apiService.getMcqSets(token,lesson_id)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<GetMcqSetsResponse>
    }
}