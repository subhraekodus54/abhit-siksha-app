package com.example.abithshiksha.model.pojo.get_orders

data class Result(
    val code: Int,
    val courses: List<Course>,
    val message: String
)