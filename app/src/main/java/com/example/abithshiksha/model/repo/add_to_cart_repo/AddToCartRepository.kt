package com.example.abithshiksha.model.repo.add_to_cart_repo

import com.example.abithshiksha.model.pojo.add_to_cart.AddToCartResponse
import com.example.abithshiksha.model.repo.Outcome

interface AddToCartRepository {
    suspend fun addToCart(course_type: Int, subjects: List<Int>, is_buy: Int, token: String): Outcome<AddToCartResponse>
}