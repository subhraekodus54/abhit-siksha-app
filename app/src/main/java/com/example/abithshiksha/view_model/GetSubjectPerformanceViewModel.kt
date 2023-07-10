package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.get_subject_performance.GetSubjectPerformanceRepository

class GetSubjectPerformanceViewModel(private val mGetSubjectPerformanceRepository: GetSubjectPerformanceRepository): ViewModel() {

    fun getSubjectDetails(
        token: String,
        id: Int
    ) = liveData {
        emit(mGetSubjectPerformanceRepository.getSubjectPerformance(token,id))
    }
}