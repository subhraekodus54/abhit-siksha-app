package com.example.abithshiksha.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.abithshiksha.model.repo.get_pdf_repo.GetPdfRepository

class GetPdfViewModel (private val mGetPdfRepository: GetPdfRepository): ViewModel() {
    fun getPdf(
        token: String,
        lesson_id: Int,
        page: Int
    ) = liveData {
        emit(mGetPdfRepository.getPdf(token, lesson_id, page))
    }
}