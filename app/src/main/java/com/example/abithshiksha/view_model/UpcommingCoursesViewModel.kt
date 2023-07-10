package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.upcomming_courses_repo.UpcommingCoursesRepository

class UpcommingCoursesViewModel (private val mUpcommingCoursesRepository: UpcommingCoursesRepository): ViewModel() {
    fun getUpcommingCourses(
        token: String,
    ) = liveData {
        emit(mUpcommingCoursesRepository.getUpcommingCourses(token))
    }
}