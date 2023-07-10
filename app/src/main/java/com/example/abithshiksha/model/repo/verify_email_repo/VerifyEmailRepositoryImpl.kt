package com.example.abithshiksha.model.repo.verify_email_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.verify_email.VerifyEmailRequest
import com.example.abithshiksha.model.pojo.verify_email.VerifyEmailResponse
import com.example.abithshiksha.model.repo.Outcome

class VerifyEmailRepositoryImpl(private val context: Context): VerifyEmailRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun verifyEmail(
        email: String,
        otp: String,
        token: String
    ): Outcome<VerifyEmailResponse> {
        val apiResponse = MutableLiveData<Outcome<VerifyEmailResponse>>()
        try {
            val response = apiService.verifyEmail(VerifyEmailRequest(email, otp), token)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<VerifyEmailResponse>
    }
}