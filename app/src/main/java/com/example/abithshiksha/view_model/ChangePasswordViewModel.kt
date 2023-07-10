package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.change_password_repo.ChangePasswordRepository

class ChangePasswordViewModel(private val mChangePasswordRepository: ChangePasswordRepository): ViewModel() {
    fun changePassword(
        old_pass: String,
        new_pass: String,
        token: String
    ) = liveData {
        emit(mChangePasswordRepository.changePassword(old_pass, new_pass, token))
    }
}