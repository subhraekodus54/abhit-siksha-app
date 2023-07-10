package com.example.abithshiksha.model.pojo.get_all_performance

data class McqPerformance(
    val accuracy: String,
    val test_attempted: Int,
    val total_attempted: Int,
    val total_correct: Int,
    val total_duration: String
)