package com.example.abithshiksha.model.pojo.payment_status

data class PaymentStatusRequest(
    val cart_id: Int,
    val razorpay_order_id: String,
    val razorpay_payment_id: String
)
