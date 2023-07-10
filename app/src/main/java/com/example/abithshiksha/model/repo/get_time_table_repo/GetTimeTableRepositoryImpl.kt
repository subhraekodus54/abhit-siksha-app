package com.example.abithshiksha.model.repo.get_time_table_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.get_time_table.GetTimeTableResponse
import com.example.abithshiksha.model.repo.Outcome

class GetTimeTableRepositoryImpl (private val context: Context): GetTimeTableRepository {
    private val apiService = ApiClient.getInstance(context)

    override suspend fun getTimeTable(token: String): Outcome<GetTimeTableResponse> {
        val apiResponse = MutableLiveData<Outcome<GetTimeTableResponse>>()
        try {
            val response = apiService.getTimeTable(token)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<GetTimeTableResponse>
    }
}