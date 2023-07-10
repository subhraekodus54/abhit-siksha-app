package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.get_time_table_repo.GetTimeTableRepository

class GetTimeTableViewModel (private val mGetTimeTableRepository: GetTimeTableRepository): ViewModel() {
    fun getTimeTable(
        token: String,
    ) = liveData {
        emit(mGetTimeTableRepository.getTimeTable(token))
    }
}