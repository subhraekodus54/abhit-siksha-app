package com.example.abithshiksha.model.pojo.puchase_history

data class ResultX(
    val board: String,
    val `class`: String,
    val course_type: String,
    val created_at: String,
    val id: Int,
    val subjects: List<Subject>,
    val total_amount: String
)