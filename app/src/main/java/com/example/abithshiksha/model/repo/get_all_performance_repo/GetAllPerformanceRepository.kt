package com.example.abithshiksha.model.repo.get_all_performance_repo

import com.example.abithshiksha.model.pojo.get_all_performance.GetAllPerformanceResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetAllPerformanceRepository {
    suspend fun getAllPerformance(token: String): Outcome<GetAllPerformanceResponse>
}