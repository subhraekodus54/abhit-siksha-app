package com.example.abithshiksha.model.repo.get_addons_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.add_ons.GetAddonsResponse
import com.example.abithshiksha.model.repo.Outcome

class GetAddonsRepositoryImpl (private val context: Context): GetAddonsRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun getAddons(
        token: String,
        board_id: Int,
        standard: String
    ): Outcome<GetAddonsResponse> {
        val apiResponse = MutableLiveData<Outcome<GetAddonsResponse>>()
        try {
            val response = apiService.getAddOns(token, board_id, standard)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }
        return apiResponse.value as Outcome<GetAddonsResponse>
    }

}