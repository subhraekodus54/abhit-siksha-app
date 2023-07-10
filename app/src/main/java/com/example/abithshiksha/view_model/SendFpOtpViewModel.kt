package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.fp_send_otp_repo.SendOtpRepository

class SendFpOtpViewModel(private val mSendOtpRepository: SendOtpRepository): ViewModel() {

    fun sendOtp(
        type: String,
        phone: String?,
        email: String?,
        token: String
    ) = liveData {
        emit(mSendOtpRepository.sendOtp(type, phone, email,token))
    }
}