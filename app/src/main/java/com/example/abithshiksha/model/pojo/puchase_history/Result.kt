package com.example.abithshiksha.model.pojo.puchase_history

data class Result(
    val code: Int,
    val message: String,
    val result: List<ResultX>,
    val status: Int
)