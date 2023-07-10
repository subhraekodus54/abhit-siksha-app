package com.example.abithshiksha.model.repo.get_order_id_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.get_order_id.GetOrderIdResponse
import com.example.abithshiksha.model.repo.Outcome

class GetOrderIdRepositoryImpl(private val context: Context): GetOrderIdRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun getOrderId(token: String, cart_id: Int): Outcome<GetOrderIdResponse> {
        val apiResponse = MutableLiveData<Outcome<GetOrderIdResponse>>()
        try {
            val response = apiService.getOrderId(token,cart_id)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<GetOrderIdResponse>
    }
}