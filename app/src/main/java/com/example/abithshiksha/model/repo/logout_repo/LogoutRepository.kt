package com.example.abithshiksha.model.repo.logout_repo

import com.example.abithshiksha.model.pojo.log_out.LogoutResponse
import com.example.abithshiksha.model.repo.Outcome

interface LogoutRepository {
    suspend fun logout(token: String): Outcome<LogoutResponse>
}