package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.sign_up_repo.SignUpRepository

class SignUpViewModel (private val mLoginRepo: SignUpRepository): ViewModel() {

    fun signUp(
        name: String,
        email: String,
        phone: String,
        password: String,
        is_above_eighteen: Int,
        assign_class_id: Int,
        board_id: Int,
        parent_name: String?
    ) = liveData {
        emit(mLoginRepo.signUp(name, email, phone, password, is_above_eighteen, assign_class_id, board_id, parent_name))
    }
}