package com.example.abithshiksha.model.pojo.login_model

data class LoginResponseModel(
    val access_token: String,
    val code: Int,
    val `data`: Data,
    val message: String,
    val status: Int,
    val token_type: String
)