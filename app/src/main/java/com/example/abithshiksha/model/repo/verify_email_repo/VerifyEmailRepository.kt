package com.example.abithshiksha.model.repo.verify_email_repo

import com.example.abithshiksha.model.pojo.verify_email.VerifyEmailResponse
import com.example.abithshiksha.model.repo.Outcome

interface VerifyEmailRepository {
    suspend fun verifyEmail(email: String, otp: String, token: String): Outcome<VerifyEmailResponse>
}