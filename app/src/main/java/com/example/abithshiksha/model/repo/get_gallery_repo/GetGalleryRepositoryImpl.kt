package com.example.abithshiksha.model.repo.get_gallery_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.get_gallery.GetGalleryResponse
import com.example.abithshiksha.model.repo.Outcome

class GetGalleryRepositoryImpl (private val context: Context): GetGalleryRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun getGallery(token: String): Outcome<GetGalleryResponse> {
        val apiResponse = MutableLiveData<Outcome<GetGalleryResponse>>()
        try {
            val response = apiService.getGallery(token)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<GetGalleryResponse>
    }
}