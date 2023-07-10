package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.get_class_list_repo.GetClassListRepository

class GetClassListViewModel(private val mGetClassListRepository: GetClassListRepository): ViewModel() {

    fun getClassList(
        token: String,
        board_name: String
    ) = liveData {
        emit(mGetClassListRepository.getClassList(token, board_name))
    }
}