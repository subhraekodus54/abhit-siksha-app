package com.example.abithshiksha.model.pojo.get_order_id

data class Data(
    val amount: Int,
    val cart_id: Int,
    val razorpayKey: String,
    val razorpayOrderId: String
)