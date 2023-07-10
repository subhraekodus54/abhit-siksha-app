package com.example.abithshiksha.model.repo.remove_cart_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.remove_cart.RemoveCartResponse
import com.example.abithshiksha.model.repo.Outcome

class RemoveCartRepositoryImpl(private val context: Context): RemoveCartRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun removeCart(token: String, cart_id: Int): Outcome<RemoveCartResponse> {
        val apiResponse = MutableLiveData<Outcome<RemoveCartResponse>>()
        try {
            val response = apiService.removeCart(token,cart_id)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<RemoveCartResponse>
    }
}