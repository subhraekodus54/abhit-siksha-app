package com.example.abithshiksha.model.repo.submit_mcq_repo

import com.example.abithshiksha.model.pojo.submit_mcq.request.Answer
import com.example.abithshiksha.model.pojo.submit_mcq.response.SubmitMcqResponse
import com.example.abithshiksha.model.repo.Outcome

interface SubmitMcqRepository {
    suspend fun submitMcq(
        set_id: String,
        start_time: String,
        end_time: String,
        answers: MutableList<Answer>,
        token: String): Outcome<SubmitMcqResponse>
}