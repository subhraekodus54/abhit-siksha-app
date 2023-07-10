package com.example.abithshiksha.model.pojo.get_carts

data class Result(
    val carts: List<Cart>,
    val code: Int,
    val message: String,
    val status: Int
)