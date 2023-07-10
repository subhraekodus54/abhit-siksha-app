package com.example.abithshiksha.model.repo.get_filtered_subject_repo

import com.example.abithshiksha.model.pojo.get_filtered_subject.GetFilteredSubjectResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetFilteredSubjectRepository {
    suspend fun getFilteredSubject(board: String, standard: String, stream: String,token: String): Outcome<GetFilteredSubjectResponse>
}