package com.example.abithshiksha.model.repo.edit_profile_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.edit_profile.EditProfileRequest
import com.example.abithshiksha.model.pojo.edit_profile.EditProfileResponse
import com.example.abithshiksha.model.repo.Outcome

class EditProfileRepositoryImpl(private val context: Context): EditProfileRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun editProfile(
        education: String?,
        gender: String?,
        address: String?,
        token: String
    ): Outcome<EditProfileResponse> {
        val apiResponse = MutableLiveData<Outcome<EditProfileResponse>>()
        try {
            val response = apiService.editProfile(EditProfileRequest(education, gender, address),token)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }
        return apiResponse.value as Outcome<EditProfileResponse>
    }
}