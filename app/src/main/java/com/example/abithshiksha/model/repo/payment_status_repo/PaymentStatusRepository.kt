package com.example.abithshiksha.model.repo.payment_status_repo

import com.example.abithshiksha.model.pojo.payment_status.PaymentStatusResponse
import com.example.abithshiksha.model.repo.Outcome

interface PaymentStatusRepository {
    suspend fun paymentStatus(cart_id: Int,
                              razorpay_order_id: String,
                              razorpay_payment_id: String,
                              token: String): Outcome<PaymentStatusResponse>
}