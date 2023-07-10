package com.example.abithshiksha.model.repo.get_mcq_repo

import com.example.abithshiksha.model.pojo.get_mcq.GetMcqResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetMcqRepository {
    suspend fun getMcq(token: String, set_id: Int, page: Int): Outcome<GetMcqResponse>
}