package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.edit_profile_repo.EditProfileRepository

class EditProfileViewModel(private val mEditProfileRepository: EditProfileRepository): ViewModel() {
    fun editProfile(
        education: String?,
        gender: String?,
        address: String?,
        token: String
    ) = liveData {
        emit(mEditProfileRepository.editProfile(education, gender, address, token))
    }
}