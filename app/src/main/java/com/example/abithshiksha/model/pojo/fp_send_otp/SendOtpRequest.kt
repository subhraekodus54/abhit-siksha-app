package com.example.abithshiksha.model.pojo.fp_send_otp

data class SendOtpRequest(
    val type: String,
    val phone: String?,
    val email: String?
)