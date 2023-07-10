package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.get_profile_repo.GetProfileRepository

class GetProfileViewModel(private val mGetProfileRepository: GetProfileRepository): ViewModel() {

    fun getProfile(
        token: String,
    ) = liveData {
        emit(mGetProfileRepository.getProfile(token))
    }
}