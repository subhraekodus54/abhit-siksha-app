package com.example.abithshiksha.model.pojo.upcomming_response

data class Result(
    val code: Int,
    val message: String,
    val result: List<ResultX>,
    val status: Int
)