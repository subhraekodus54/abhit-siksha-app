package com.example.abithshiksha.model.pojo.get_topic_list

data class Result(
    val code: Int,
    val message: String,
    val result: List<ResultX>
)