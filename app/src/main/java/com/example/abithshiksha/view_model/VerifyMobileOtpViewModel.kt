package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.verify_mobile_otp_repo.VerifyMobileOtpRepository

class VerifyMobileOtpViewModel(private val mVerifyMobileOtpRepository: VerifyMobileOtpRepository): ViewModel() {

    fun verifyMobileOtp(
        phone: String,
        otp: String,
        token: String,
    ) = liveData {
        emit(mVerifyMobileOtpRepository.verifyMobileOtp(phone, otp, token))
    }
}