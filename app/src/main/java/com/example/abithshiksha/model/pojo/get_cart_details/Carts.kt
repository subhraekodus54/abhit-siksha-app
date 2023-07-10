package com.example.abithshiksha.model.pojo.get_cart_details

data class Carts(
    val board: String,
    val cart_subject_details: List<CartSubjectDetail>,
    val class_name: String,
    val id: Int,
    val total_amount: Int,
    val type: String,
    val user_id: Int
)