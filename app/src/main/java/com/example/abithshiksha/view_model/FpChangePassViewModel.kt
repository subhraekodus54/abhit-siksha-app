package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.fp_change_pass_repo.FpChangePassRepository

class FpChangePassViewModel(private val mFpChangePassRepository: FpChangePassRepository): ViewModel() {
    fun changePass(
        user_id: String,
        password: String
    ) = liveData {
        emit(mFpChangePassRepository.changePass(user_id, password))
    }
}