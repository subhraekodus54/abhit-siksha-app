package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.get_course_subject_repo.GetCourseSubjectRepository

class GetCourseSubjectViewModel(private val mGetCourseSubjectRepository: GetCourseSubjectRepository): ViewModel() {
    fun getSubjects(
        token: String,
        cart_id: Int
    ) = liveData {
        emit(mGetCourseSubjectRepository.getSubjects(token, cart_id))
    }
}