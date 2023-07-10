package com.example.abithshiksha.model.repo.add_review_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.add_review.AddReviewRequest
import com.example.abithshiksha.model.pojo.add_review.AddReviewResponse
import com.example.abithshiksha.model.repo.Outcome

class AddReviewRepositoryImpl(private val context: Context): AddReviewRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun addReview(
        subject_id: Int,
        review: String,
        rating: String,
        token: String
    ): Outcome<AddReviewResponse> {
        val apiResponse = MutableLiveData<Outcome<AddReviewResponse>>()
        try {
            val response = apiService.addReview(AddReviewRequest(subject_id, review, rating),token)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }
        return apiResponse.value as Outcome<AddReviewResponse>
    }
}