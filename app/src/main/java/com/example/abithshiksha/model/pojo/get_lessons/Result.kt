package com.example.abithshiksha.model.pojo.get_lessons

data class Result(
    val code: Int,
    val message: String,
    val result: ResultX,
    val total_lesson: Int
)