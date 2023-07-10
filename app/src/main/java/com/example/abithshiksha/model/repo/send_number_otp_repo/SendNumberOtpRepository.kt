package com.example.abithshiksha.model.repo.send_number_otp_repo

import com.example.abithshiksha.model.pojo.send_number_otp.SendNumberOtpResponse
import com.example.abithshiksha.model.repo.Outcome

interface SendNumberOtpRepository {
    suspend fun sendNumberOtp(token: String, phone: String): Outcome<SendNumberOtpResponse>
}