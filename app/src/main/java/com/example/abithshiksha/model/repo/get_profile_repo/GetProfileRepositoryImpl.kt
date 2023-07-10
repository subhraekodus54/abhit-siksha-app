package com.example.abithshiksha.model.repo.get_profile_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.get_profile.GetProfileResponse
import com.example.abithshiksha.model.repo.Outcome

class GetProfileRepositoryImpl(private val context: Context): GetProfileRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun getProfile(token: String): Outcome<GetProfileResponse> {
        val apiResponse = MutableLiveData<Outcome<GetProfileResponse>>()
        try {
            val response = apiService.getProfile(token)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<GetProfileResponse>
    }
}