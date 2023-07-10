package com.example.abithshiksha.model.pojo.get_article

data class Result(
    val code: Int,
    val message: String,
    val result: ResultX,
    val status: Int
)