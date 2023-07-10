package com.example.abithshiksha.model.repo.login_repo

import com.example.abithshiksha.model.pojo.login_model.LoginResponseModel
import com.example.abithshiksha.model.repo.Outcome

interface LoginRepository {
    suspend fun login(email: String, password: String): Outcome<LoginResponseModel>
}