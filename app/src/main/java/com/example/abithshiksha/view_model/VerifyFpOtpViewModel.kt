package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.fp_verify_otp_repo.VerifyOtpRepository

class VerifyFpOtpViewModel(private val mVerifyOtpRepository: VerifyOtpRepository): ViewModel() {

    fun verifyOtp(
        user_id: String,
        otp: String
    ) = liveData {
        emit(mVerifyOtpRepository.verifyOtp(user_id, otp))
    }
}