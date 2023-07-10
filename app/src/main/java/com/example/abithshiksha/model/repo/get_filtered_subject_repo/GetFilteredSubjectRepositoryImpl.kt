package com.example.abithshiksha.model.repo.get_filtered_subject_repo

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.abithshiksha.model.network.ApiClient
import com.example.abithshiksha.model.pojo.get_filtered_subject.GetFilteredSubjectResponse
import com.example.abithshiksha.model.pojo.get_filtered_subject.GetFilteredSubjectsRequest
import com.example.abithshiksha.model.repo.Outcome

class GetFilteredSubjectRepositoryImpl(private val context: Context): GetFilteredSubjectRepository {
    private val apiService = ApiClient.getInstance(context)
    override suspend fun getFilteredSubject(
        board: String,
        standard: String,
        stream: String,
        token: String
    ): Outcome<GetFilteredSubjectResponse> {
        val apiResponse = MutableLiveData<Outcome<GetFilteredSubjectResponse>>()
        try {
            val response = apiService.getFilteredSubjects(GetFilteredSubjectsRequest(board, standard, stream),token)
            apiResponse.value = Outcome.success(response!!)
        } catch (e: Throwable) {
            apiResponse.value = Outcome.failure(e)
        }

        return apiResponse.value as Outcome<GetFilteredSubjectResponse>
    }
}