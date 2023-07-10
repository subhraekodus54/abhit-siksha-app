package com.example.abithshiksha.model.repo.get_mcq_set_repo

import com.example.abithshiksha.model.pojo.get_mcq_sets.GetMcqSetsResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetMcqSetsRepository {
    suspend fun getMcqSets(token: String, lesson_id: Int): Outcome<GetMcqSetsResponse>
}