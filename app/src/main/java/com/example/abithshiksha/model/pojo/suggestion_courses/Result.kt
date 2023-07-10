package com.example.abithshiksha.model.pojo.suggestion_courses

data class Result(
    val code: Int,
    val message: String,
    val result: List<ResultX>,
    val status: Int
)