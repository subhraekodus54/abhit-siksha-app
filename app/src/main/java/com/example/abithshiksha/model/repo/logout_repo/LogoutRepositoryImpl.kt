package com.example.abithshiksha.model.repo.logout_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.log_out.LogoutResponse
import com.example.abithshiksha.model.repo.Outcome

class LogoutRepositoryImpl(private val context: Context): LogoutRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun logout(token: String): Outcome<LogoutResponse> {
        val apiResponse = MutableLiveData<Outcome<LogoutResponse>>()
        try {
            val response = apiService.logout(token)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<LogoutResponse>
    }
}