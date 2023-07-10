package com.example.abithshiksha.model.repo.verify_mobile_otp_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.verify_mobile_otp.VerifyMobileOtpRequest
import com.example.abithshiksha.model.pojo.verify_mobile_otp.VerifyMobileOtpResponse
import com.example.abithshiksha.model.repo.Outcome

class VerifyMobileOtpRepositoryImpl(private val context: Context): VerifyMobileOtpRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun verifyMobileOtp(
        phone: String,
        otp: String,
        token: String
    ): Outcome<VerifyMobileOtpResponse> {
        val apiResponse = MutableLiveData<Outcome<VerifyMobileOtpResponse>>()
        try {
            val response = apiService.verifyMobileOtp(VerifyMobileOtpRequest(phone, otp), token)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<VerifyMobileOtpResponse>
    }
}