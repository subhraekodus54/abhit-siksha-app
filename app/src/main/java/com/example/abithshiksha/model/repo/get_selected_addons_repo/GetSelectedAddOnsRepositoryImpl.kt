package com.example.abithshiksha.model.repo.get_selected_addons_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.get_addons.GetSelectedAddonsResponse
import com.example.abithshiksha.model.repo.Outcome

class GetSelectedAddOnsRepositoryImpl(private val context: Context): GetSelectedAddOnsRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun getSelectedAddons(token: String): Outcome<GetSelectedAddonsResponse> {
        val apiResponse = MutableLiveData<Outcome<GetSelectedAddonsResponse>>()
        try {
            val response = apiService.getSelectedAddOns(token)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<GetSelectedAddonsResponse>
    }
}