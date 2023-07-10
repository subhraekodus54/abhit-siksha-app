package com.example.abithshiksha.model.repo.fp_send_otp_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.fp_send_otp.SendOtpRequest
import com.example.abithshiksha.model.pojo.fp_send_otp.SendOtpResponse
import com.example.abithshiksha.model.repo.Outcome

class SendOtpRepositoryImpl(private val context: Context): SendOtpRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun sendOtp(
        type: String,
        phone: String?,
        email: String?,
        token: String
    ): Outcome<SendOtpResponse> {
        val apiResponse = MutableLiveData<Outcome<SendOtpResponse>>()
        try {
            val response = apiService.fpSendOtp(SendOtpRequest(type, phone, email))
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }
        return apiResponse.value as Outcome<SendOtpResponse>
    }
}