package com.example.abithshiksha.model.repo.sign_up_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.login_model.LoginRequest
import com.example.abithshiksha.model.pojo.login_model.LoginResponseModel
import com.example.abithshiksha.model.pojo.sign_up.SignUpRequest
import com.example.abithshiksha.model.pojo.sign_up.SignUpResponse
import com.example.abithshiksha.model.repo.Outcome

class SignUpRepositoryImpl(private val context: Context): SignUpRepository {

    private val apiService = ApiClient.getInstance(context)
    override suspend fun signUp(
        name: String,
        email: String,
        phone: String,
        password: String,
        is_above_eighteen: Int,
        assign_class_id: Int,
        board_id: Int,
        parent_name: String?
    ): Outcome<SignUpResponse> {
        val apiResponse = MutableLiveData<Outcome<SignUpResponse>>()
        try {
            val response = apiService.signup(SignUpRequest(name, email, phone, password, is_above_eighteen, assign_class_id, board_id, parent_name))
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<SignUpResponse>
    }
}