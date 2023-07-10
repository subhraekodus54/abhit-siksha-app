package com.example.abithshiksha.model.repo.verify_mobile_otp_repo

import com.example.abithshiksha.model.pojo.verify_mobile_otp.VerifyMobileOtpResponse
import com.example.abithshiksha.model.repo.Outcome

interface VerifyMobileOtpRepository {
    suspend fun verifyMobileOtp(phone: String, otp: String, token: String): Outcome<VerifyMobileOtpResponse>
}