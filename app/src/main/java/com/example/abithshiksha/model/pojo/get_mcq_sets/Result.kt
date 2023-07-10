package com.example.abithshiksha.model.pojo.get_mcq_sets

data class Result(
    val code: Int,
    val message: String,
    val result: List<ResultX>,
    val status: Int
)