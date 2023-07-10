package com.example.abithshiksha.model.pojo.verify_email

data class VerifyEmailRequest(
    val email: String,
    val otp: String
)