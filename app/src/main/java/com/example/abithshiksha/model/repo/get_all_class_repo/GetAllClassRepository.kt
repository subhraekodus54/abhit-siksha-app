package com.example.abithshiksha.model.repo.get_all_class_repo

import com.example.abithshiksha.model.pojo.get_all_class.GetAllClassResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetAllClassRepository {
    suspend fun getAllClass(board_id: Int): Outcome<GetAllClassResponse>
}