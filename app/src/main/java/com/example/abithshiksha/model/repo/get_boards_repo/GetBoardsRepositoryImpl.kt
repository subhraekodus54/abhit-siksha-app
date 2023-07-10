package com.example.abithshiksha.model.repo.get_boards_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.get_boards.GetBoardsResponse
import com.example.abithshiksha.model.repo.Outcome

class GetBoardsRepositoryImpl(private val context: Context): GetBoardsRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun getBoards(): Outcome<GetBoardsResponse> {
        val apiResponse = MutableLiveData<Outcome<GetBoardsResponse>>()
        try {
            val response = apiService.getBoards()
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<GetBoardsResponse>
    }
}