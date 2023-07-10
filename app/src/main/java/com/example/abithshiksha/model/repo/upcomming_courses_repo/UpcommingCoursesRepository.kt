package com.example.abithshiksha.model.repo.upcomming_courses_repo

import com.example.abithshiksha.model.pojo.upcomming_response.GetUpcommingCoursesResponse
import com.example.abithshiksha.model.repo.Outcome

interface UpcommingCoursesRepository {
    suspend fun getUpcommingCourses(token: String): Outcome<GetUpcommingCoursesResponse>
}