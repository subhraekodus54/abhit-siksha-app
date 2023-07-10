package com.example.abithshiksha.model.repo.send_email_otp_repo

import com.example.abithshiksha.model.pojo.send_email_otp.SendEmailOtpResponse
import com.example.abithshiksha.model.repo.Outcome

interface SendEmailOtpRepository {
    suspend fun sendEmailOtp(email: String, token: String): Outcome<SendEmailOtpResponse>
}