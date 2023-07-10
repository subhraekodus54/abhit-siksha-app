package com.example.abithshiksha.model.repo.get_lessons_repo

import com.example.abithshiksha.model.pojo.get_lessons.GetLessonsResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetLessonsRepository {
    suspend fun getLessons(token: String, subject_id: Int, page: Int): Outcome<GetLessonsResponse>
}