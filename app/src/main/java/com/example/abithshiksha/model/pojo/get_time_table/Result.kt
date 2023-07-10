package com.example.abithshiksha.model.pojo.get_time_table

data class Result(
    val code: Int,
    val message: String,
    val result: List<ResultX>,
    val status: Int
)