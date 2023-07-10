package com.example.abithshiksha.model.pojo.fp_verify_otp

data class VerifyOtpRequest(
    val user_id: String,
    val otp: String
)