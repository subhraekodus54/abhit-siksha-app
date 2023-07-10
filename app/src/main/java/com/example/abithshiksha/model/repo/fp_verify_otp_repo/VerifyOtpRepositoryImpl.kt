package com.example.abithshiksha.model.repo.fp_verify_otp_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.fp_verify_otp.VerifyOtpRequest
import com.example.abithshiksha.model.pojo.fp_verify_otp.VerifyOtpResponse
import com.example.abithshiksha.model.repo.Outcome

class VerifyOtpRepositoryImpl(private val context: Context): VerifyOtpRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun verifyOtp(user_id: String, otp: String): Outcome<VerifyOtpResponse> {
        val apiResponse = MutableLiveData<Outcome<VerifyOtpResponse>>()
        try {
            val response = apiService.fpVerifyOtp(VerifyOtpRequest(user_id, otp))
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }
        return apiResponse.value as Outcome<VerifyOtpResponse>
    }
}