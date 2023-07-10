package com.example.abithshiksha.model.repo.get_time_table_repo

import com.example.abithshiksha.model.pojo.get_time_table.GetTimeTableResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetTimeTableRepository {
    suspend fun getTimeTable(token: String): Outcome<GetTimeTableResponse>
}