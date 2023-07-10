package com.example.abithshiksha.model.repo.upload_profile_pic_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.upload_profile_pic.UploadProfilePicResponse
import com.example.abithshiksha.model.repo.Outcome
import okhttp3.MultipartBody

class UploadProfilePicRepositoryImpl(context: Context) : UploadProfilePicRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun uploadProfilePic(
        image: MultipartBody.Part?,
        token: String
    ): Outcome<UploadProfilePicResponse> {
        val apiResponse = MutableLiveData<Outcome<UploadProfilePicResponse>>()
        try {
            val response = apiService.uploadProfilePic(
                image,
                token,
            )

            apiResponse.value = Outcome.success(response!!)

        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<UploadProfilePicResponse>
    }
}