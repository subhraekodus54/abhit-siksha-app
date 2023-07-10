package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.get_all_class_repo.GetAllClassRepository

class GetAllClassViewModel (private val mGetAllClassRepository: GetAllClassRepository): ViewModel() {
    fun getAllClass(
        board_id: Int
    ) = liveData {
        emit(mGetAllClassRepository.getAllClass(board_id))
    }
}