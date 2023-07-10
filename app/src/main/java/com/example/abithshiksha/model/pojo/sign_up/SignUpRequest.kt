package com.example.abithshiksha.model.pojo.sign_up

data class SignUpRequest(
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val is_above_eighteen: Int,
    val assign_class_id: Int,
    val board_id: Int,
    val parent_name: String? = null
)