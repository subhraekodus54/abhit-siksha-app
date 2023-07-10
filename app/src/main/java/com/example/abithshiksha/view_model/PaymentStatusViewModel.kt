package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.payment_status_repo.PaymentStatusRepository

class PaymentStatusViewModel(private val mPaymentStatusRepository: PaymentStatusRepository): ViewModel() {
    fun paymentStatus(
        cart_id: Int,
        razorpay_order_id: String,
        razorpay_payment_id: String,
        token: String,
    ) = liveData {
        emit(mPaymentStatusRepository.paymentStatus(cart_id, razorpay_order_id, razorpay_payment_id, token))
    }
}