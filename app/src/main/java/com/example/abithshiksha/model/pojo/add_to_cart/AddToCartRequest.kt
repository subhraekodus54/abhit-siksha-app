package com.example.abithshiksha.model.pojo.add_to_cart

data class AddToCartRequest(
    val course_type: Int,
    val subjects: List<Int>,
    val is_buy: Int,
    val addons: List<Int>
)