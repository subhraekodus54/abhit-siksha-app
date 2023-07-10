package com.example.abithshiksha.model.pojo.fp_send_otp

data class Result(
    val code: Int,
    val message: String,
    val otp: Int,
    val user_id: Int
)