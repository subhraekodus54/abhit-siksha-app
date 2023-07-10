package com.example.abithshiksha.model.pojo.upcomming_response

data class ResultX(
    val assign_class: AssignClass,
    val assign_class_id: Int,
    val board_id: Int,
    val boards: Boards,
    val id: Int,
    val image: String,
    val rating: String,
    val subject_amount: Int,
    val subject_name: String,
)