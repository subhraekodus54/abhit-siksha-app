package com.example.abithshiksha.model.repo.add_to_cart_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.add_to_cart.AddToCartRequest
import com.example.abithshiksha.model.pojo.add_to_cart.AddToCartResponse
import com.example.abithshiksha.model.repo.Outcome

class AddToCartRepositoryImpl(private val context: Context): AddToCartRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun addToCart(
        course_type: Int,
        subjects: List<Int>,
        is_buy: Int,
        addons: List<Int>,
        token: String
    ): Outcome<AddToCartResponse> {
        val apiResponse = MutableLiveData<Outcome<AddToCartResponse>>()
        try {
            val response = apiService.addToCart(AddToCartRequest(course_type,subjects,is_buy,addons),token)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }
        return apiResponse.value as Outcome<AddToCartResponse>
    }
}