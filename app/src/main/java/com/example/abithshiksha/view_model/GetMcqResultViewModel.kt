package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.get_mcq_result_repo.GetMcqResultRepository

class GetMcqResultViewModel(private val mGetMcqResultRepository: GetMcqResultRepository): ViewModel() {
    fun getMcqResult(
        token: String,
        id: Int
    ) = liveData {
        emit(mGetMcqResultRepository.getMcqResult(token, id))
    }
}