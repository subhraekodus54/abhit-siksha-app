package com.example.abithshiksha.model.pojo.sign_up

data class SignUpResponse(
    val access_token: String,
    val `data`: Data,
    val message: String,
    val status: Int,
    val token_type: String
)