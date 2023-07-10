package com.example.abithshiksha.model.pojo.send_email_otp

data class Result(
    val code: Int,
    val `data`: Data,
    val message: String,
    val status: Int
)