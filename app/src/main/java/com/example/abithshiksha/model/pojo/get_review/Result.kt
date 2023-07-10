package com.example.abithshiksha.model.pojo.get_review

data class Result(
    val code: Int,
    val message: String,
    val reviews: List<Review>
)