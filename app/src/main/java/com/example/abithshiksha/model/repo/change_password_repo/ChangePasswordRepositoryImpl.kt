package com.example.abithshiksha.model.repo.change_password_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.change_password.ChangePasswordRequest
import com.example.abithshiksha.model.pojo.change_password.ChangePasswordResponse
import com.example.abithshiksha.model.repo.Outcome

class ChangePasswordRepositoryImpl(private val context: Context): ChangePasswordRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun changePassword(
        old_pass: String,
        new_pass: String,
        token: String
    ): Outcome<ChangePasswordResponse> {
        val apiResponse = MutableLiveData<Outcome<ChangePasswordResponse>>()
        try {
            val response = apiService.changePassword(ChangePasswordRequest(old_pass,new_pass),token)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }
        return apiResponse.value as Outcome<ChangePasswordResponse>
    }
}