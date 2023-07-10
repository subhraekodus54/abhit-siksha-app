package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.upload_profile_pic_repo.UploadProfilePicRepository
import okhttp3.MultipartBody

class UploadProfilePicViewModel (private val uploadProfilePicRepository: UploadProfilePicRepository) : ViewModel() {

    fun uploadProfilePic(image: MultipartBody.Part?,
                         token: String) = liveData {

        emit(uploadProfilePicRepository.uploadProfilePic(image,token))
    }
}