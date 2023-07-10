package com.example.abithshiksha.model.repo.get_subject_performance

import com.example.abithshiksha.model.pojo.get_subject_performance.GetSubjectPerformanceResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetSubjectPerformanceRepository {
    suspend fun getSubjectPerformance(token: String, id: Int): Outcome<GetSubjectPerformanceResponse>
}