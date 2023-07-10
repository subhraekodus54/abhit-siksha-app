package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.login_repo.LoginRepository

class LoginViewModel (private val mLoginRepo: LoginRepository): ViewModel() {

    fun login(
        email: String,
        password: String,
    ) = liveData {
        emit(mLoginRepo.login(email, password))
    }
}