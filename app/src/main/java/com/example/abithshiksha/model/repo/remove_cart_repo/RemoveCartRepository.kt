package com.example.abithshiksha.model.repo.remove_cart_repo

import com.example.abithshiksha.model.pojo.remove_cart.RemoveCartResponse
import com.example.abithshiksha.model.repo.Outcome

interface RemoveCartRepository{
    suspend fun removeCart(token: String, cart_id: Int): Outcome<RemoveCartResponse>
}