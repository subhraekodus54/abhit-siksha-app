package com.example.abithshiksha.model.repo.get_mcq_result_repo

import com.example.abithshiksha.model.pojo.mcq_result.GetMcqResultResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetMcqResultRepository {
    suspend fun getMcqResult(token: String, id: Int): Outcome<GetMcqResultResponse>
}