package com.example.abithshiksha.model.repo.get_suggestion_courses

import com.example.abithshiksha.model.pojo.suggestion_courses.GetSuggestionCoursesResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetSuggestionCoursesRepository {
    suspend fun getSuggestionCourses(token: String): Outcome<GetSuggestionCoursesResponse>
}