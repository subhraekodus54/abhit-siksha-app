package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.get_mcq_repo.GetMcqRepository

class GetMcqViewModel(private val mGetMcqRepository: GetMcqRepository): ViewModel() {
    fun getMcq(
        token: String,
        set_id: Int,
        page: Int
    ) = liveData {
        emit(mGetMcqRepository.getMcq(token, set_id, page))
    }
}