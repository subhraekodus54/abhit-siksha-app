package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.get_all_performance_repo.GetAllPerformanceRepository

class GetAllPerformanceViewModel(private val mGetAllPerformanceRepository: GetAllPerformanceRepository): ViewModel() {
    fun getAllPerformance(
        token: String
    ) = liveData {
        emit(mGetAllPerformanceRepository.getAllPerformance(token))
    }
}