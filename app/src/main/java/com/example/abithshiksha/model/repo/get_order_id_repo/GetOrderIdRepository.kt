package com.example.abithshiksha.model.repo.get_order_id_repo

import com.example.abithshiksha.model.pojo.get_order_id.GetOrderIdResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetOrderIdRepository {
    suspend fun getOrderId(token: String, cart_id: Int): Outcome<GetOrderIdResponse>
}