package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.logout_repo.LogoutRepository

class LogoutViewModel(private val mLogoutRepository: LogoutRepository): ViewModel() {
    fun logout(
        token: String,
    ) = liveData {
        emit(mLogoutRepository.logout(token))
    }
}