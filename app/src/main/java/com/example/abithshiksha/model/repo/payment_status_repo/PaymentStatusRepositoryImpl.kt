package com.example.abithshiksha.model.repo.payment_status_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.payment_status.PaymentStatusRequest
import com.example.abithshiksha.model.pojo.payment_status.PaymentStatusResponse
import com.example.abithshiksha.model.pojo.remove_cart.RemoveCartResponse
import com.example.abithshiksha.model.repo.Outcome

class PaymentStatusRepositoryImpl(private val context: Context): PaymentStatusRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun paymentStatus(
        cart_id: Int,
        razorpay_order_id: String,
        razorpay_payment_id: String,
        token: String
    ): Outcome<PaymentStatusResponse> {
        val apiResponse = MutableLiveData<Outcome<PaymentStatusResponse>>()
        try {
            val response = apiService.paymentStatus(PaymentStatusRequest(cart_id,razorpay_order_id, razorpay_payment_id),token)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<PaymentStatusResponse>
    }
}