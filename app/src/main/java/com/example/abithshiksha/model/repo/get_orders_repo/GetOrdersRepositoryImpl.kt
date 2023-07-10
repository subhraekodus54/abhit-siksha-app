package com.example.abithshiksha.model.repo.get_orders_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.get_orders.GetOrdersResponse
import com.example.abithshiksha.model.repo.Outcome

class GetOrdersRepositoryImpl(private val context: Context): GetOrdersRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun getOrders(token: String): Outcome<GetOrdersResponse> {
        val apiResponse = MutableLiveData<Outcome<GetOrdersResponse>>()
        try {
            val response = apiService.getOrders(token)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<GetOrdersResponse>
    }
}