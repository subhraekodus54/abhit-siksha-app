package com.example.abithshiksha.model.pojo.get_orders

data class Course(
    val board: String,
    val cart_subject_details: List<String>,
    val class_name: String,
    val id: Int,
    val total_amount: Int,
    val total_subject: Int,
    val type: String,
    val user_id: Int,
    val board_logo: String
)