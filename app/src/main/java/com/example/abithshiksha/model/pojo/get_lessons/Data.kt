package com.example.abithshiksha.model.pojo.get_lessons

data class Data(
    val board_id: Int,
    val board_name: String,
    val class_id: Int,
    val class_name: String,
    val id: Int,
    val name: String,
    val subject_id: Int,
    val subject_name: String,
    val total_content: TotalContent
)