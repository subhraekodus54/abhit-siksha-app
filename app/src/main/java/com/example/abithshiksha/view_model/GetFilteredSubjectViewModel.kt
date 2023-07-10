package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.get_filtered_subject_repo.GetFilteredSubjectRepository

class GetFilteredSubjectViewModel(private val mGetFilteredSubjectRepository: GetFilteredSubjectRepository): ViewModel() {

    fun getFilteredSubject(
        board: String,
        standard: String,
        stream: String,
        token: String
    ) = liveData {
        emit(mGetFilteredSubjectRepository.getFilteredSubject(board, standard, stream, token))
    }
}