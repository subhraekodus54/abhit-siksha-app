package com.example.abithshiksha.model.repo.send_number_otp_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.send_number_otp.SendNumberOtpRequest
import com.example.abithshiksha.model.pojo.send_number_otp.SendNumberOtpResponse
import com.example.abithshiksha.model.repo.Outcome

class SendNumberOtpRepositoryImpl(private val context: Context): SendNumberOtpRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun sendNumberOtp(
        token: String,
        phone: String
    ): Outcome<SendNumberOtpResponse> {
        val apiResponse = MutableLiveData<Outcome<SendNumberOtpResponse>>()
        try {
            val response = apiService.sendNumberOtp(SendNumberOtpRequest(phone), token)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<SendNumberOtpResponse>
    }
}