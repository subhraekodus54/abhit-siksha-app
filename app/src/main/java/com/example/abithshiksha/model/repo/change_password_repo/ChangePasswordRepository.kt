package com.example.abithshiksha.model.repo.change_password_repo

import com.example.abithshiksha.model.pojo.change_password.ChangePasswordResponse
import com.example.abithshiksha.model.repo.Outcome

interface ChangePasswordRepository {
    suspend fun changePassword(old_pass: String, new_pass: String, token: String): Outcome<ChangePasswordResponse>
}