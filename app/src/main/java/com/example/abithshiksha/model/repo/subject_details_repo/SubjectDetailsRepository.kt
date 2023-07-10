package com.example.abithshiksha.model.repo.subject_details_repo

import com.example.abithshiksha.model.pojo.subject_details.GetSubjectDetailsResponse
import com.example.abithshiksha.model.repo.Outcome

interface SubjectDetailsRepository {
    suspend fun getSubjectDetails(token: String, subject_id: Int): Outcome<GetSubjectDetailsResponse>
}