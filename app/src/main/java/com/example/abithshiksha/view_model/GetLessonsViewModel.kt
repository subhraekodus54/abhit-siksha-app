package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.get_lessons_repo.GetLessonsRepository

class GetLessonsViewModel(private val mGetLessonsRepository: GetLessonsRepository): ViewModel() {
    fun getLessons(
        token: String,
        subject_id: Int,
        page: Int
    ) = liveData {
        emit(mGetLessonsRepository.getLessons(token, subject_id, page))
    }
}