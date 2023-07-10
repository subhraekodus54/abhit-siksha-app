package com.example.abithshiksha.model.repo.get_pdf_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.get_pdf.GetPdfResponse
import com.example.abithshiksha.model.repo.Outcome

class GetPdfRepositoryImpl(private val context: Context): GetPdfRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun getPdf(token: String, lesson_id: Int, page: Int): Outcome<GetPdfResponse> {
        val apiResponse = MutableLiveData<Outcome<GetPdfResponse>>()
        try {
            val response = apiService.getPdf(token,lesson_id,page)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<GetPdfResponse>
    }
}