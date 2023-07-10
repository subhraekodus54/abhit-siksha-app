package com.example.abithshiksha.model.repo.get_cart_details_repo

import com.example.abithshiksha.model.pojo.get_cart_details.GetCartDetailsResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetCartDetailsRepository {
    suspend fun getCartDetails(token: String, cart_id: Int): Outcome<GetCartDetailsResponse>
}