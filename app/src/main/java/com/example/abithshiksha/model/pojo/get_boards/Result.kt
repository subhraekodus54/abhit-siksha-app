package com.example.abithshiksha.model.pojo.get_boards

data class Result(
    val board: List<Board>,
    val code: Int,
    val message: String,
    val status: Int
)