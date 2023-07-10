package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.get_review_repo.GetReviewRepository

class GetReviewViewModel(private val mGetReviewRepository: GetReviewRepository): ViewModel() {
    fun getReview(
        token: String,
        subject_id: Int
    ) = liveData {
        emit(mGetReviewRepository.getReview(token,subject_id))
    }
}