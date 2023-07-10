package com.example.abithshiksha.model.repo.get_class_list_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.get_class_list.GetClassListResponse
import com.example.abithshiksha.model.repo.Outcome

class GetClassListRepositoryImpl(private val context: Context): GetClassListRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun getClassList(
        token: String,
        board_name: String
    ): Outcome<GetClassListResponse> {
        val apiResponse = MutableLiveData<Outcome<GetClassListResponse>>()
        try {
            val response = apiService.getClass(token, board_name)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<GetClassListResponse>
    }
}