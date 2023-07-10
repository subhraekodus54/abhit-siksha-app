package com.example.abithshiksha.model.pojo.get_mcq

data class McqQuestion(
    val correct_answer: String,
    val id: Int,
    val options: List<String>,
    val question: String
)