package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.send_number_otp_repo.SendNumberOtpRepository

class SendNumberViewModel (private val mSendNumberOtpRepository: SendNumberOtpRepository): ViewModel() {

    fun sendNumber(
        token: String,
        phone: String
    ) = liveData {
        emit(mSendNumberOtpRepository.sendNumberOtp(token, phone))
    }
}