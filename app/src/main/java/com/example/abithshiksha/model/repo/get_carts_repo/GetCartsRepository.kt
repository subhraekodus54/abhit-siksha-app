package com.example.abithshiksha.model.repo.get_carts_repo

import com.example.abithshiksha.model.pojo.get_carts.GetCartsResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetCartsRepository {
    suspend fun getCart(token: String): Outcome<GetCartsResponse>
}