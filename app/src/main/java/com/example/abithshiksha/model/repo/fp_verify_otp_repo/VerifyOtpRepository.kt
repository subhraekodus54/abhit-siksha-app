package com.example.abithshiksha.model.repo.fp_verify_otp_repo

import com.example.abithshiksha.model.pojo.fp_verify_otp.VerifyOtpResponse
import com.example.abithshiksha.model.repo.Outcome

interface VerifyOtpRepository {
    suspend fun verifyOtp(user_id: String,
                        otp: String): Outcome<VerifyOtpResponse>
}