package com.example.abithshiksha.model.repo.add_review_repo

import com.example.abithshiksha.model.pojo.add_review.AddReviewResponse
import com.example.abithshiksha.model.repo.Outcome

interface AddReviewRepository {
    suspend fun addReview(subject_id:Int,
                          review: String,
                          rating: String,
                          token: String): Outcome<AddReviewResponse>
}