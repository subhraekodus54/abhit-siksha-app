package com.example.abithshiksha.model.pojo.submit_mcq.response

data class Result(
    val code: Int,
    val message: String,
    val result: ResultX,
    val status: Int
)