package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.add_review_repo.AddReviewRepository

class AddReviewViewModel(private val mAddToCartRepository: AddReviewRepository): ViewModel() {
    fun addReview(
        subject_id: Int,
        review: String,
        rating: String,
        token: String
    ) = liveData {
        emit(mAddToCartRepository.addReview(subject_id, review, rating, token))
    }
}