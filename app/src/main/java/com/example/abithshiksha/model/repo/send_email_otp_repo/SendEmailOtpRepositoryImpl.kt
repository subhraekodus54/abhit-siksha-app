package com.example.abithshiksha.model.repo.send_email_otp_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.send_email_otp.SendEmailOtpRequest
import com.example.abithshiksha.model.pojo.send_email_otp.SendEmailOtpResponse
import com.example.abithshiksha.model.repo.Outcome

class SendEmailOtpRepositoryImpl(private val context: Context): SendEmailOtpRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun sendEmailOtp(email: String, token: String): Outcome<SendEmailOtpResponse> {
        val apiResponse = MutableLiveData<Outcome<SendEmailOtpResponse>>()
        try {
            val response = apiService.sendEmailOtp(SendEmailOtpRequest(email), token)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<SendEmailOtpResponse>
    }
}