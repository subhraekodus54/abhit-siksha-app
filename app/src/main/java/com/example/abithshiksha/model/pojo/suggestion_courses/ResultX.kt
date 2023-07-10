package com.example.abithshiksha.model.pojo.suggestion_courses

data class ResultX(
    val assign_class: AssignClass,
    val assign_class_id: Int,
    val board_id: Int,
    val boards: Boards,
    val id: Int,
    val image: String,
    val subject_amount: Int,
    val subject_name: String,
    val rating: String,
)