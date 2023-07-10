package com.example.abithshiksha.model.pojo.get_purchase_receipt

data class Result(
    val code: Int,
    val invoice_url: String,
    val message: String,
    val status: Int
)