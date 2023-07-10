package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.verify_email_repo.VerifyEmailRepository

class VerifyEmailViewModel(private val mVerifyEmailRepository: VerifyEmailRepository): ViewModel() {
    fun verifyEmail(
        email: String,
        otp: String,
        token: String,
    ) = liveData {
        emit(mVerifyEmailRepository.verifyEmail(email, otp, token))
    }
}