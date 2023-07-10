package com.example.abithshiksha.model.repo.upload_profile_pic_repo

import com.example.abithshiksha.model.pojo.upload_profile_pic.UploadProfilePicResponse
import com.example.abithshiksha.model.repo.Outcome
import okhttp3.MultipartBody

interface UploadProfilePicRepository {
    suspend fun uploadProfilePic(image: MultipartBody.Part?,
                                 token: String): Outcome<UploadProfilePicResponse>
}