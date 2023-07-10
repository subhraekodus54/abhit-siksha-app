package com.example.abithshiksha.model.repo.get_class_list_repo

import com.example.abithshiksha.model.pojo.get_class_list.GetClassListResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetClassListRepository {
    suspend fun getClassList(token: String, board_name: String): Outcome<GetClassListResponse>
}