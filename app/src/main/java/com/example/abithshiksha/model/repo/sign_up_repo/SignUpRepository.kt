package com.example.abithshiksha.model.repo.sign_up_repo

import com.example.abithshiksha.model.pojo.sign_up.SignUpResponse
import com.example.abithshiksha.model.repo.Outcome

interface SignUpRepository {
    suspend fun signUp(name: String,
                       email: String,
                       phone: String,
                       password: String,
                       is_above_eighteen: Int,
                       assign_class_id: Int,
                       board_id: Int,
                       parent_name: String? = null
    ): Outcome<SignUpResponse>
}