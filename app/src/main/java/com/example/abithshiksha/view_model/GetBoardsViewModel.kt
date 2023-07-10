package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.get_boards_repo.GetBoardsRepository

class GetBoardsViewModel(private val mGetBoardsRepository: GetBoardsRepository): ViewModel() {

    fun getBoards() = liveData {
        emit(mGetBoardsRepository.getBoards())
    }
}