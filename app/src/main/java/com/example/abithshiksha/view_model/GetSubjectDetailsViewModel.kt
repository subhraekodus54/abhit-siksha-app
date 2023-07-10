package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.subject_details_repo.SubjectDetailsRepository

class GetSubjectDetailsViewModel(private val mSubjectDetailsRepository: SubjectDetailsRepository): ViewModel() {

    fun getSubjectDetails(
        token: String,
        subject_id: Int
    ) = liveData {
        emit(mSubjectDetailsRepository.getSubjectDetails(token,subject_id))
    }
}