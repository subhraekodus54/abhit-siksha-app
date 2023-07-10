package com.example.abithshiksha.model.repo.get_review_repo

import com.example.abithshiksha.model.pojo.get_review.GetReviewResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetReviewRepository {
    suspend fun getReview(token: String, subject_id: Int): Outcome<GetReviewResponse>
}