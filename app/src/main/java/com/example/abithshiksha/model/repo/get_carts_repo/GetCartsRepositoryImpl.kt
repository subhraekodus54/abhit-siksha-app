package com.example.abithshiksha.model.repo.get_carts_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.get_carts.GetCartsResponse
import com.example.abithshiksha.model.repo.Outcome

class GetCartsRepositoryImpl(private val context: Context): GetCartsRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun getCart(token: String): Outcome<GetCartsResponse> {
        val apiResponse = MutableLiveData<Outcome<GetCartsResponse>>()
        try {
            val response = apiService.getCart(token)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }
        return apiResponse.value as Outcome<GetCartsResponse>
    }
}