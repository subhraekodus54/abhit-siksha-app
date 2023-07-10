package com.example.abithshiksha.model.repo.get_orders_repo

import com.example.abithshiksha.model.pojo.get_orders.GetOrdersResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetOrdersRepository {
    suspend fun getOrders(token: String): Outcome<GetOrdersResponse>
}