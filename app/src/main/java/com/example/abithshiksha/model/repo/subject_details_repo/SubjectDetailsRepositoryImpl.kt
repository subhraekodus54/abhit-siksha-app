package com.example.abithshiksha.model.repo.subject_details_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.subject_details.GetSubjectDetailsResponse
import com.example.abithshiksha.model.repo.Outcome

class SubjectDetailsRepositoryImpl(private val context: Context): SubjectDetailsRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun getSubjectDetails(token: String, subject_id: Int): Outcome<GetSubjectDetailsResponse> {
        val apiResponse = MutableLiveData<Outcome<GetSubjectDetailsResponse>>()
        try {
            val response = apiService.getSubjectDetails(token, subject_id)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<GetSubjectDetailsResponse>
    }
}