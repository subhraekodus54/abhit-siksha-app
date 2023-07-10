package com.example.abithshiksha.model.repo.fp_change_pass_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.fp_change_pass.FpChangePassRequest
import com.example.abithshiksha.model.pojo.fp_change_pass.FpChangePassResponse
import com.example.abithshiksha.model.repo.Outcome

class FpChangePassRepositoryImpl(private val context: Context): FpChangePassRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun changePass(
        user_id: String,
        password: String
    ): Outcome<FpChangePassResponse> {
        val apiResponse = MutableLiveData<Outcome<FpChangePassResponse>>()
        try {
            val response = apiService.fpChangePass(FpChangePassRequest(user_id, password))
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }
        return apiResponse.value as Outcome<FpChangePassResponse>
    }
}