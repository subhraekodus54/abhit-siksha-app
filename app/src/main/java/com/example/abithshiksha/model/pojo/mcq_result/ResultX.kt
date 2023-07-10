package com.example.abithshiksha.model.pojo.mcq_result

data class ResultX(
    val attempted_question: Int,
    val correct_attempted: Int,
    val incorrect_attempted: Int,
    val set_title: String,
    val total_question: Int
)