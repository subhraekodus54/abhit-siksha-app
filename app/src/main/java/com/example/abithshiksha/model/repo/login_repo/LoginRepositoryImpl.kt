package com.example.abithshiksha.model.repo.login_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.login_model.LoginRequest
import com.example.abithshiksha.model.pojo.login_model.LoginResponseModel
import com.example.abithshiksha.model.repo.Outcome

class LoginRepositoryImpl (private val context: Context): LoginRepository {

    private val apiService = ApiClient.getInstance(context)
    override suspend fun login(email: String, password: String): Outcome<LoginResponseModel> {
        val apiResponse = MutableLiveData<Outcome<LoginResponseModel>>()
        try {
            val response = apiService.login(LoginRequest(email, password))
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<LoginResponseModel>
    }
}