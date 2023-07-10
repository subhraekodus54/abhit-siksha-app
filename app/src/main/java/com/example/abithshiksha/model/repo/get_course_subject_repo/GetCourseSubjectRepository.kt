package com.example.abithshiksha.model.repo.get_course_subject_repo

import com.example.abithshiksha.model.pojo.get_course_subject.GetCourseSubjectResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetCourseSubjectRepository {
    suspend fun getSubjects(token: String, cart_id: Int): Outcome<GetCourseSubjectResponse>
}