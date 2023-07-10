package com.example.abithshiksha.model.repo.fp_send_otp_repo

import com.example.abithshiksha.model.pojo.fp_send_otp.SendOtpResponse
import com.example.abithshiksha.model.repo.Outcome

interface SendOtpRepository {
    suspend fun sendOtp(type: String,
                            phone: String?,
                            email: String?,
                            token: String): Outcome<SendOtpResponse>
}