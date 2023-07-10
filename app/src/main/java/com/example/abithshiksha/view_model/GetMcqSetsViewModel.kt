package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.get_mcq_set_repo.GetMcqSetsRepository

class GetMcqSetsViewModel(private val mGetMcqSetsRepository: GetMcqSetsRepository): ViewModel() {
    fun getMcqSets(
        token: String,
        lesson_id: Int
    ) = liveData {
        emit(mGetMcqSetsRepository.getMcqSets(token, lesson_id))
    }
}