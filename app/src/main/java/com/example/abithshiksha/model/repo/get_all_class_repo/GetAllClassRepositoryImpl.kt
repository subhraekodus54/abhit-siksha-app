package com.example.abithshiksha.model.repo.get_all_class_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.get_all_class.GetAllClassResponse
import com.example.abithshiksha.model.repo.Outcome

class GetAllClassRepositoryImpl (private val context: Context): GetAllClassRepository {
    private val apiService = ApiClient.getInstance(context)

    override suspend fun getAllClass(board_id: Int): Outcome<GetAllClassResponse> {
        val apiResponse = MutableLiveData<Outcome<GetAllClassResponse>>()
        try {
            val response = apiService.getAllClass(board_id)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }
        return apiResponse.value as Outcome<GetAllClassResponse>
    }
}