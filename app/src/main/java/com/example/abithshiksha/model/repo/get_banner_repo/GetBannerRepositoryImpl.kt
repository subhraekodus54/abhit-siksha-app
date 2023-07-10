package com.example.abithshiksha.model.repo.get_banner_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.banner.GetBannerResponse
import com.example.abithshiksha.model.repo.Outcome

class GetBannerRepositoryImpl (private val context: Context): GetBannerRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun getBanner(token: String): Outcome<GetBannerResponse> {
        val apiResponse = MutableLiveData<Outcome<GetBannerResponse>>()
        try {
            val response = apiService.getBanner(token)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<GetBannerResponse>
    }
}