package com.example.abithshiksha.model.repo.get_boards_repo

import com.example.abithshiksha.model.pojo.get_boards.GetBoardsResponse
import com.example.abithshiksha.model.repo.Outcome

interface GetBoardsRepository {
    suspend fun getBoards(): Outcome<GetBoardsResponse>
}