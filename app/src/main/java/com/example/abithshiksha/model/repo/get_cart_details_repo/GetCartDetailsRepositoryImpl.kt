package com.example.abithshiksha.model.repo.get_cart_details_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.get_cart_details.GetCartDetailsResponse
import com.example.abithshiksha.model.repo.Outcome

class GetCartDetailsRepositoryImpl(private val context: Context): GetCartDetailsRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun getCartDetails(
        token: String,
        cart_id: Int
    ): Outcome<GetCartDetailsResponse> {
        val apiResponse = MutableLiveData<Outcome<GetCartDetailsResponse>>()
        try {
            val response = apiService.getCartDetails(token, cart_id)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }
        return apiResponse.value as Outcome<GetCartDetailsResponse>
    }
}