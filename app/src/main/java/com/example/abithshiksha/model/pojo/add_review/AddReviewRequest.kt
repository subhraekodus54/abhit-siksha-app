package com.example.abithshiksha.model.pojo.add_review

data class AddReviewRequest(
    val subject_id: Int,
    val review: String,
    val rating: String
)
