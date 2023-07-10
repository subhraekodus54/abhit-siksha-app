package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.get_suggestion_courses.GetSuggestionCoursesRepository

class GetSuggestionCoursesViewModel(private val mGetSuggestionCoursesRepository: GetSuggestionCoursesRepository): ViewModel() {
    fun getSuggestionCourses(
        token: String,
    ) = liveData {
        emit(mGetSuggestionCoursesRepository.getSuggestionCourses(token))
    }
}