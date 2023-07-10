package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.send_email_otp_repo.SendEmailOtpRepository

class SendEmailOtpViewModel(private val mSendEmailOtpRepository: SendEmailOtpRepository): ViewModel() {

    fun sendEmailOtp(
        email: String,
        token: String
    ) = liveData {
        emit(mSendEmailOtpRepository.sendEmailOtp(email, token))
    }
}