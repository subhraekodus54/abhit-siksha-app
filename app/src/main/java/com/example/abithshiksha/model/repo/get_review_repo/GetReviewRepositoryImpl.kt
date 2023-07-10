package com.example.abithshiksha.model.repo.get_review_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.get_review.GetReviewResponse
import com.example.abithshiksha.model.repo.Outcome

class GetReviewRepositoryImpl(private val context: Context): GetReviewRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun getReview(token: String, subject_id: Int): Outcome<GetReviewResponse> {
        val apiResponse = MutableLiveData<Outcome<GetReviewResponse>>()
        try {
            val response = apiService.getReview(token,subject_id)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<GetReviewResponse>
    }
}