package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.pojo.submit_mcq.request.Answer
import com.example.abithshiksha.model.repo.submit_mcq_repo.SubmitMcqRepository

class SubmitMcqViewModel(private val mSubmitMcqRepository: SubmitMcqRepository): ViewModel() {

    fun submitMcq(
        set_id: String,
        start_time: String,
        end_time: String,
        answers: MutableList<Answer>,
        token: String
    ) = liveData {
        emit(mSubmitMcqRepository.submitMcq(set_id,start_time,end_time,answers,token))
    }
}