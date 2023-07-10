package com.example.abithshiksha.model.repo.submit_mcq_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.submit_mcq.request.Answer
import com.example.abithshiksha.model.pojo.submit_mcq.request.SubmitMcqRequest
import com.example.abithshiksha.model.pojo.submit_mcq.response.SubmitMcqResponse
import com.example.abithshiksha.model.repo.Outcome

class SubmitMcqRepositoryImpl(private val context: Context): SubmitMcqRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun submitMcq(
        set_id: String,
        start_time: String,
        end_time: String,
        answers: MutableList<Answer>,
        token: String
    ): Outcome<SubmitMcqResponse> {
        val apiResponse = MutableLiveData<Outcome<SubmitMcqResponse>>()
        try {
            val response = apiService.submitMcq(SubmitMcqRequest(set_id,start_time,end_time,answers),token)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<SubmitMcqResponse>
    }
}